import numpy as np
import keras.backend as K
from keras.layers import Dense,Input,Lambda
from keras.models import Model,Layer
from keras.optimizers import Nadam
from keras import metrics

input_shape = 100
hide_1 = 64
hide_2 = 30
hide_3 = 20
batch = 10
activation = 'softplus'
epsilon_std = 1.0

X = np.random.randint(0,6,size=(1000,input_shape))


x = Input(shape=(input_shape,))
h = Dense(hide_1, activation=activation)(x)
z_mean = Dense(hide_2)(h)
z_log_var = Dense(hide_2)(h)


def sampling(args):
    z_mean, z_log_var = args
    epsilon = K.random_normal(shape=(K.shape(z_mean)[0], hide_2), mean=0.,
                              stddev=epsilon_std)
    return z_mean + K.exp(z_log_var / 2) * epsilon

# note that "output_shape" isn't necessary with the TensorFlow backend
z = Lambda(sampling, output_shape=(hide_2,))([z_mean, z_log_var])

# we instantiate these layers separately so as to reuse them later
decoder_h = Dense(hide_1, activation=activation)
decoder_mean = Dense(input_shape, activation=activation)
h_decoded = decoder_h(z)
x_decoded_mean = decoder_mean(h_decoded)

# Custom loss layer
class CustomVariationalLayer(Layer):
    def __init__(self, **kwargs):
        self.is_placeholder = True
        super(CustomVariationalLayer, self).__init__(**kwargs)

    # [|0-y_true|*(1/(y_true+1e-6))*(y_true-y_pre)]**2
    def my_loss(self, x, x_decoded_mean,e = K.epsilon()):
        return K.mean(K.square(x * (1 / (x + e)) * (x - x_decoded_mean)), axis=-1)

    def call(self, inputs):
        x = inputs[0]
        x_decoded_mean = inputs[1]
        loss = self.my_loss(x, x_decoded_mean)
        self.add_loss(loss, inputs=inputs)
        # We won't actually use the output.
        return x_decoded_mean

y = CustomVariationalLayer()([x, x_decoded_mean])
vae = Model(x, y)
vae.compile(optimizer=Nadam(), loss=None)

vae.fit(X,epochs=200,batch_size=batch,shuffle=True,validation_data=(X,None))
X_test = X[:3]
# print('test loss:',vae.evaluate(X))
pred = vae.predict(X_test)
# print('\npredict:\n',pred)
# print('\naccu:\n',X_test)
t = np.concatenate((pred.T,X_test.T),axis=1)
print(t)
