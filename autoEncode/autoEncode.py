import numpy as np
import keras.backend as K
from keras.layers import Dense,Input
from keras.models import Sequential,Model
from keras.optimizers import Nadam

# [|0-y_true|*(1/(y_true+1e-6))*(y_true-y_pre)]**2
def my_loss(y_true,y_pre,e=1e-6):
    return K.mean(K.square((y_true-0)*(1/(y_true+e))*(y_true-y_pre)),axis=-1)

input_shape = 50
hide_1 = 40
hide_2 = 30
hide_3 = 10
batch = 10

X = np.random.randint(0,6,size=(600,input_shape))

x = Input(shape=(input_shape,))
encode= Dense(hide_1,activation='tanh')(x)
encode= Dense(hide_2,activation='tanh')(encode)
encoder_output = Dense(hide_3)(encode)

decode = Dense(hide_2,activation='tanh')(encoder_output)
decode = Dense(hide_1,activation='tanh')(decode)
decode = Dense(input_shape,activation='selu')(decode)

autoencoder = Model(input=x, output=decode)

autoencoder.compile(optimizer=Nadam(), loss=my_loss)
autoencoder.fit(X, X,
                epochs=200,
                batch_size=batch,
                shuffle=True)
X_test = X[:3]
print('test loss:',autoencoder.evaluate(X_test,X_test))
pred = autoencoder.predict(X_test)
# print('\npredict:\n',pred)
# print('\naccu:\n',X_test)
t = np.concatenate((pred.T,X_test.T),axis=1)
print(t)