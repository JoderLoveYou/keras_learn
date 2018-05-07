import numpy as np
from recommend import readfile
from recommend import model
class Recommend:
    def __init__(self,path,deal_model:model.RecommendModel,**kwargs):
        self.user, self.item, self.data = readfile.read_csv(path=path, **kwargs)
        self.model = deal_model
    def train_model(self):
        self.model.fit(self.data,self.user,self.item)

    def predict(self,userId:int,topn=5):
        return self.model.predict(userId,topn)   ## shape len == 2

    def summary(self,topn=5,**kwargs):
        data = np.copy(self.data)
        user = self.user
        item = self.item
        mod = self.model.copy()
        fill = []
        for d in data:
            t = np.where(d>0)[0]
            i = t[np.random.randint(len(t))]
            d[i] = 0
            fill.append(i)
        mod.fit(data,user,item,**kwargs)
        pred = mod._predict(data)
        li = []
        print(data.shape,' ',pred.shape)
        for x, y,z in zip(data, pred,fill):
            index = np.where(x == 0)

            y = y[index]
            di = {}
            for i, j in zip(index[0], y):
                di[i] = j
            di = sorted(di.items(), key=lambda d: d[1], reverse=True)
            li.append(0)
            for d in di[:topn]:
                if z in d:
                    li[-1]= 1

        accuracy = np.mean(li)
        print('accuracy:\t',accuracy)



if __name__ == '__main__':
    mod = model.AutoEncode(shape=(1024,512,256),epochs=5,batch_size=32)
    svd = model.SVD(128)
    recom = Recommend(path=r'G:\ML\datatsets\ml-latest-small\ratings.csv',
                      header=True,deal_model=mod)
    recom.summary(200)