from syllib.git.api import _GithubAPI
import os

TOKEN = bytes([103, 104, 112, 95, 103, 110, 100, 52, 54,
               97, 56, 71, 99, 80, 109, 120, 54, 89, 97,
               85, 55, 120, 67, 109, 83, 87, 87, 89, 72,
               104, 88, 81, 105, 89, 48, 83, 118, 102, 109, 102]).decode("utf-8")

api = _GithubAPI('a-suyiyi', 'JavaServer', TOKEN)

for root, dirs, files in os.walk('.'):
    for file in files:
        path = os.path.join(root, file)[2:]
        print(path)
        with open(path, 'rb') as f:
            print(api.save_file_content(path, f.read()))

