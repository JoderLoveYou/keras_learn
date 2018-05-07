import csv
import numpy as np
def read_csv(path,encoding = 'utf-8',user_column = 0,item_column=1,header=False):
    '''

    :param path:str, file path
    :param encoding:str, charset
    :param user_column: int, where user column
    :param item_column: int, where item column
    :param header: bool, whether has header
    :return: user(np.array):user idex,item(np.array):item index,data(np.array) user_item score matrix
    '''
    csv_reader = csv.reader(open(path, 'r', encoding=encoding))
    user = set()
    item = set()
    rows = []
    for row in csv_reader:
        if header and csv_reader.line_num == 1:continue
        user.add(int(row[user_column]))
        item.add(int(row[item_column]))
        rows.append(row)
    data = np.zeros(shape=(len(user), len(item)), dtype=np.float32)
    user = list(user)
    item = list(item)
    for row in rows:
        data[user.index(int(row[0]))][item.index(int(row[1]))] = float(row[2])
    return np.array(user),np.array(item),data



if __name__ == '__main__':
    user,item,data = read_csv(r'G:\ML\datatsets\ml-latest-small\ratings.csv',header=True)
    data = data[1]
    print(data[np.where(data>0.1)])