import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns



df = pd.read_csv('insurance.csv')
#print(df)


# EDA
print("Shape\n")
print(df.shape)
print("Head\n")
print(df.head())
print("info\n")
df.info #data type
print("Describe\n")
df.describe() #gives numeric values and mean median 


# Null values
print(df.isnull().sum())

print(df.columns)

# Numeric vaar

num_cols = ['age', 'bmi', 'children', 'charges']
for cols in num_cols:
    plt.figure(figsize=(6,4))
    sns.histplot(df[cols],kde=True, bins=20)
    
    
# Col var
sns.catplot(x = df['children'])
sns.catplot(x = df['smoker'])

# Relations between col vars and identifying pattern bw insurance charges and smokers
for cols in num_cols:
    plt.figure(figsize= (6,4))
    sns.boxplot(x= df[cols])




x= df.iloc[:, :-1].values #all the rows, what rows means range [col, rows]
y = df.iloc[:, -1].values

from sklearn.impute import SimpleImputer
# Handling Missing values "Numeric" or we can use fillna()
imputer = SimpleImputer(missing_values = np.nan,strategy='mean') #
imputer.fit(x[:, 1:3])#filters where to apply imputing
x[:, 1:3 ]=imputer.transform(x[:, 1:3])# apply changes


# Encoding categorial variables
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder

# consturter->args (transformers[what kid of transforming is needed][0]? and remainder[not apply transformrs])
ct= ColumnTransformer(transformers=[('PreProcessing_encoder',OneHotEncoder(),[0])], remainder='passthrough')

x_encoded = np.array(ct.fit_transform([x]))# its a list but cast it to array

#Label encode when you have only 2 variables
from sklearn.preprocessing import LabelEncoder

le = LabelEncoder( )
y_encoded = le.fit_transform(y)


# Split making sure we have 70,15,15 % of data
from sklearn.model_selection import train_test_split
xtrain,xtemp, ytrain,ytemp= train_test_split(x,y,train_size=0.3, random_state=1)
xtest,xval,ytest,yval = train_test_split(xtemp,ytemp,test_size=0.5,random_state=1)


# Feature Scaling
from sklearn.preprocessing import StandardScaler
sc = StandardScaler()

xtrain[:, 3:] = sc.fit_transform(xtrain[:, :3])
#for test we need the same scaler so ony transform, reason in notebook
xtest[:, 3:] = sc.transform(xtest[:, :3])













