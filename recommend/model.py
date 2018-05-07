import numpy as np
import keras.backend as K
from keras.layers import Dense,Input
from keras.models import Model
import tensorflow as tf
import abc

def my_loss(y_true,y_pre,e=K.epsilon()):
    square = K.square(y_true*(1/(y_true+e))*(y_true-y_pre))
    square = tf.where(tf.not_equal(square,0),square,square)
    return K.mean(square,axis=-1)

def topN(y_true,y_pred,topn=5):
    index = np.where(y_true == 0)
    y_pred = y_pred[index]
    di = {}
    for i, j in zip(index[0], y_pred):
        di[i] = j
    di = sorted(di.items(), key=lambda d: d[1], reverse=True)
    return di[:topn]

class RecommendModel(object):
    __metaclass__ = abc.ABCMeta
    def __init__(self,**kwargs):
        pass

    @abc.abstractmethod
    def fit(self,data,user,item,**kwargs):
        pass

    @abc.abstractmethod
    def predict(self,userId:int,**kwargs):
        pass

    @abc.abstractmethod
    def _predict(self,data):
        pass

    @abc.abstractmethod
    def summary(self,**kargs):
        pass

    @abc.abstractmethod
    def copy(self):
        return self

class AutoEncode(RecommendModel):
    def __init__(self,shape = (100,),batch_size = 10,
                 activation = 'softplus',epochs=200,optimizer='nadam',**kwargs):
        if len(shape)==0:raise RuntimeError("Thre length of shape is lower than one.")
        self.shape = shape
        self.activation = activation
        self.batch_size = batch_size
        self.epochs = epochs
        self.optimizer = optimizer
    def fit(self,data,user,item,**kargs):
        '''
        :param data: numpy
        :return:
        '''
        self.user = user
        self.item = item
        self.data = data
        x = Input(shape=(data.shape[-1],))
        encode = None
        for index,val in enumerate(self.shape):
            if index == 0:
                encode = Dense(val, activation=self.activation)(x)
            else:
                encode = Dense(val, activation=self.activation)(encode)
        decode = None
        for index, val in enumerate(self.shape[1:][::-1]):
            if index == 0:
                decode = Dense(val, activation=self.activation)(encode)
            else:
                decode = Dense(val, activation=self.activation)(decode)
        decode = Dense(data.shape[-1], activation='selu')(decode)

        self.autoencoder = Model(input=x, output=decode)
        self.autoencoder.compile(optimizer=self.optimizer, loss=my_loss)
        self.autoencoder.fit(data, data,
                        epochs=self.epochs,
                        batch_size=self.batch_size,
                        shuffle=False)

    def predict(self,userId:int,topn=5):
        '''
        :param X: numpy
        :return:
        '''
        X = self.data[np.argmax(self.user == userId)]  ## shape len ==1
        if len(X.shape)>2 and len(X.shape)<1:
            raise RuntimeError("X shape length is error,must 1 or 2")
        elif len(X.shape) ==1:
            X = X[np.newaxis,:]
        pred = self.autoencoder.predict(X)
        pred = pred[0]
        return topN(X,pred,topn)

    def _predict(self,data):
        return self.autoencoder.predict(data)

    def summary(self,**kargs):
        return self.autoencoder.summary(**kargs)

    def copy(self):
        return self


class SVD(RecommendModel):
    def __init__(self,feature=100,):
        self.feature = feature

    def fit(self,data,user,item,**kargs):
        self.user = user
        self.item = item
        self.data = data
        U, V = self.svd(data,feature=self.feature,**kargs)
        self.glod = np.dot(U,V.T)

    def predict(self, userId: int, topn=5):
        X = self.data[np.argmax(self.user == userId)]
        y = self.glod[np.argmax(self.user == userId)]
        return topN(X,y,topn)

    def _predict(self,data):
        return self.glod

    def summary(self, **kargs):
        return 'svd\'s feature is %s'%(self.feature)

    def copy(self):
        return self

    def svd(self,mat, feature, steps=20, gama=0.02, lamda=0.3):
        slowRate = 0.99
        preRmse = 1000000000.0
        nowRmse = 0.0

        user_feature = np.matrix(np.random.rand(mat.shape[0], feature))
        item_feature = np.matrix(np.random.rand(mat.shape[1], feature))

        for step in range(steps):
            rmse = 0.0
            n = 0
            for u in range(mat.shape[0]):
                for i in range(mat.shape[1]):
                    if not np.isnan(mat[u, i]) and mat[u, i] !=0:
                        pui = float(np.dot(user_feature[u, :], item_feature[i, :].T))
                        eui = mat[u, i] - pui
                        rmse += pow(eui, 2)
                        n += 1
                        for k in range(feature):
                            user_feature[u, k] += gama * (eui * item_feature[i, k] - lamda * user_feature[u, k])
                            item_feature[i, k] += gama * (
                            eui * user_feature[u, k] - lamda * item_feature[i, k])  # 原blog这里有错误

            nowRmse = np.sqrt(rmse * 1.0 / n)
            print('step: %d      Rmse: %s' % ((step + 1), nowRmse))
            if (nowRmse < preRmse):
                preRmse = nowRmse
            else:break  # 这个退出条件其实还有点问题
            gama *= slowRate
            step += 1

        return user_feature, item_feature


if __name__ == '__main__':
    a = [[1, 2, 3], [2, 4, 0]]
    a = np.array(a)
    u,v = SVD().svd(mat=a,feature=2)
    print(np.dot(u,v.T))
