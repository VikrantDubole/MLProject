from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split

def preprocess_data(df):

    X = df.drop("Class", axis = 1)
    y = df["Class"]

X_train, y_train, X_test, y_test = train_test_split(X,y, train_size=0.7, random_state=42, stratify=y)

scaler = StandardScaler()

X_train["Class", "Amount"] = scaler.fit_transform(X_train["Class", "Amount"])

X_test["Class", "Amount"] = scaler.transform(X_test["Class", "Amount"])