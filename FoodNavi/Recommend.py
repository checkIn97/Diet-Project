import sys
import os
import pandas as pd
import warnings; warnings.filterwarnings('ignore')

my_data_raw = pd.DataFrame({'user_useq':[sys.argv[1]], 'user_sex':[sys.argv[2]], 'user_age':[sys.argv[3]], 'user_height':[sys.argv[4]], 'user_weight':[sys.argv[5]]})
my_data = my_data_raw[['user_sex', 'user_age', 'user_height', 'user_weight']]

import pandas as pd
import numpy as np
history_data = pd.read_csv('History.csv', encoding='utf-8')
user_data = history_data[['user_sex', 'user_age', 'user_height', 'user_weight']]
serve_data = history_data[['meal_type', 'served_date']]
food_data = history_data[['food_kcal', 'food_carb', 'food_prt', 'food_fat']]

def assign_meal_type_value(x, hour):
    if x == '구분없음':
        if 5 <= int(hour) <= 9:
            x = '아침'
        elif 11 <= int(hour) <= 15:
            x = '점심'
        elif 17 <= int(hour) <= 21:
            x = '저녁'
        else:
            x = '간식'
    if x == '아침':
        x = 1
    elif x == '점심':
        x = 2
    elif x == '저녁':
        x = 3
    elif x == '간식':
        x = 4
    else:
        x = 0
    return x

def assign_user_sex_value(x):
    if x == 'm':
        return 1
    elif x == 'f':
        return 2
    return 0

def getDay(x):
    a = list(x.split())
    return a[0]

def getHour(x):
    a = list(x.split())
    return int(a[3][:2])

def getFromNow(x):
    return x

my_data['user_sex'] = my_data['user_sex'].apply(assign_user_sex_value)
user_data['user_sex'] = user_data['user_sex'].apply(assign_user_sex_value)
serve_data['day'] = serve_data['served_date'].apply(getDay)
serve_data['hour'] = serve_data['served_date'].apply(getHour)

for i in serve_data.index:
    hour = serve_data['hour'][i]
    if 6 <= hour <= 9:
        serve_data['meal_type'] = '아침'
    elif 11 <= hour <= 14:
        serve_data['meal_type'] = '점심'
    elif 17 <= hour <= 20:
        serve_data['meal_type'] = '저녁'
    else:
        serve_data['meal_type'] = '간식'

serve_data

# 성별과 나이대를 좀 더 강하게 적용하는 방법을 추가해야 함

# 표준화된 데이터를 사용
user_mean = user_data.mean()
user_std = user_data.std()

my_data_nor = my_data.copy()
for col in my_data.columns:
    my_data_nor[col] = abs(float(my_data[col])-user_mean[col]) / user_std[col]

user_data_nor = user_data.copy()
for col in user_data.columns:
    user_data_nor[col] = abs(user_data_nor[col] - user_mean[col])
    user_data_nor[col] /= user_std[col]

from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity



sim_user_data = cosine_similarity(my_data_nor, user_data_nor)
sim_user_data_sorted_index = sim_user_data[0].argsort()[::-1]

food_feature_list = food_data.loc[sim_user_data_sorted_index[:]]

user_score_list = []
for i in range(len(user_data_nor)):
    user_score_list.append(sim_user_data[0][sim_user_data_sorted_index[i]])
food_feature_score = pd.DataFrame({'score':user_score_list})
food_feature_list['score'] = user_score_list
food_feature_list

import pandas as pd
import numpy as np
food_data_raw = pd.read_csv('Food.csv', encoding='utf-8')
food_data_raw.dropna(inplace=True)
food_data_mean = food_data_raw[['kcal', 'carb', 'prt', 'fat']].mean()
food_data_std = food_data_raw[['kcal', 'carb', 'prt', 'fat']].std()


# 필터링 입력

food_data_filtered = food_data_raw
food_data_filtered

from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

cols = ['kcal', 'carb', 'prt', 'fat']
food_data_filtered_for_test = food_data_filtered[cols]
food_feature_list.columns = ['kcal', 'carb', 'prt', 'fat', 'score']
food_feature_list.reset_index(inplace=True)
food_feature_list.drop('index', axis= 1, inplace=True)

food_feature_list_for_test = food_feature_list[cols]

for n in cols:
    food_feature_list[n] = abs(food_feature_list[n] - food_data_mean[n]) / food_data_std[n]

final_food_list = []
final_food_score = []

for i in range(len(food_feature_list_for_test)):
    sim_food_data = cosine_similarity(food_feature_list_for_test.iloc[i:i+1], food_data_filtered_for_test)
    sim_food_data_sorted_index = sim_food_data[0].argsort()[::-1]
    check = 0
    count = 0
    for j in range(len(sim_food_data[0])):
        idx = sim_food_data_sorted_index[j]
        tmp_score = sim_food_data[0][idx]
        if tmp_score >= 0.99:
            final_food_list.append(idx+1)
            final_food_score.append(tmp_score*food_feature_list.iloc[i]['score'])
        else:
            check = 1
            break
        if len(final_food_list) >= 100:
            check = 1
            break
        count += 1
        if count == 10:
            break
    if check == 1:
        break

final_food_recommend_list = pd.DataFrame({'fseq':final_food_list, 'score':final_food_score})
final_food_recommend_list

final_food_recommend_list.sort_values(by='score', ascending=False, inplace=True)

print(final_food_recommend_list[:60])
