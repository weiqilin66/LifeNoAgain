from flask import Flask, abort, request, jsonify

app = Flask(__name__)

# 测试数据暂时存放
tasks = []
@app.route('/', methods=['GET', 'POST'])
def home():
    return '<h1>hello world</h1>'

def main():
    app.run(host="0.0.0.0", port=8383, debug=True)
    print('api初始化完成')