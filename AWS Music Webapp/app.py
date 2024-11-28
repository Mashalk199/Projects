from flask import Flask,render_template,request,flash,redirect,url_for,session
import boto3
from botocore.exceptions import ClientError
from boto3.dynamodb.conditions import Key
from markupsafe import Markup


app = Flask(__name__)
app.secret_key = 'the_secret_key'

@app.route('/')
def login():
    return render_template('login.html')

@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('login'))

@app.route('/register')
def register():
    return render_template('register.html')

@app.route('/home')
def home():
    if session.get('email'):
        # query = ''
        # subscription = ''
        # if session.get('query_html'):
        #     query = session['query_html']
        #     print(query,'QUERY')
        # if session.get('subscription_html'):
        #     subscription = session['subscription_html']
        return render_template('home.html',user_name = session['user_name'])
    else:
        return redirect(url_for('login'))

@app.route('/about')
def about():
    return 'About content stuff goes here!'

@app.route("/addAccount",methods = ["POST"])
def addAccount():
    email = request.form['email'] # Gets the user credentials from the HTML form
    user_name = request.form['username']
    password = request.form['password']

    dynamodb = boto3.resource('dynamodb',
        region_name='us-east-1')
    table = dynamodb.Table('login')
    # Code here cited from week 4 lab -  
    # IEEE ref: RMIT, “Exercise 4 - AWS Database Services (Python),” in Week 4. 
    try:
        response = table.get_item(Key={'email':email})
    except ClientError as e:
        print(e.response['Error']['Message'])
    else: 
        if 'Item' not in response: #If the query for the email returns nothing for response, then the email doesn't exist and we can add the user
            print("Adding user:", email, user_name, password)
            table.put_item(Item={
                'email': email,
                'user_name': user_name,
                'password': password,
            })
            # stores user's details in the current session
            session['email'] = email 
            session['user_name'] = user_name 
            session['password'] = password 

            return redirect(url_for('home'))

        else:
            flash("This email already exists")
            return redirect(url_for('register'))

@app.route("/checkLogin",methods = ["POST"])
def checkLogin():
    email = request.form['email']
    password = request.form['password']
    login = get_item({'email':email},'login')
    if login: # Checks if email exists
        if login['password'] == password: # if so, checks passwords
            # flash('Login successful, pass is:'+login['password'])
            session['email'] = email 
            session['user_name'] = login['user_name']
            session['password'] = password 
            return redirect(url_for('home'))
        else:
            flash('email or password is invalid')
            return redirect(url_for('login'))
    else:
        flash('email or password is invalid')
        return redirect(url_for('login'))


def get_item(pair,table, dynamodb=None):
    if not dynamodb:
        dynamodb = boto3.resource('dynamodb',region_name='us-east-1')
    table = dynamodb.Table(table)
    try:
        response = table.get_item(Key=pair)
    except ClientError as e:
        print(e.response['Error']['Message'])
    else:
        # print(response)
        if 'Item' in response:
            return response['Item']
        else:
            return None
@app.route("/subscribe",methods = ["POST","GET"])
def subscribe():
    # print('THIS HERE:',request.args.get('title'))
    return redirect(url_for('home'))
@app.route("/makeQuery",methods = ["POST"])
def makeQuery():
    title = request.form.get('title',None)
    artist = request.form.get('artist',None)
    year = request.form.get('year',None)
    if title:
        title.strip()
    if artist:
        artist.strip()
    if year:
        year.strip()

    dynamodb = boto3.resource('dynamodb',region_name='us-east-1')
    table = dynamodb.Table('music')
    scan_kwargs = {
    'ProjectionExpression': "artist, title, #yr, img_url, web_url",
    'ExpressionAttributeNames': {"#yr": "year"}
    }
    if artist and title and year:
        scan_kwargs['FilterExpression'] = Key('artist').eq(artist) & Key('title').eq(title) & Key('year').eq(year)
    elif artist and title:
        scan_kwargs['FilterExpression'] = Key('artist').eq(artist) & Key('title').eq(title)
    elif artist and year:
        scan_kwargs['FilterExpression'] = Key('artist').eq(artist) & Key('year').eq(year)
    elif title and year:
        scan_kwargs['FilterExpression'] = Key('title').eq(title) & Key('year').eq(year)
    elif artist:
        scan_kwargs['FilterExpression'] = Key('artist').eq(artist)
    elif title:
        scan_kwargs['FilterExpression'] = Key('title').eq(title)
    elif year:
        scan_kwargs['FilterExpression'] = Key('year').eq(year)
    else:
        pass
    done = False
    start_key = None
    while not done:
        if start_key:
            scan_kwargs['ExclusiveStartKey'] = start_key
        response = table.scan(**scan_kwargs)
        start_key = response.get('LastEvaluatedKey', None)
        done = start_key is None
    results = response['Items']

    html = ''
    print(results)
    
    for result in results:
        html += '<tr>'
        html += '<td>' + result['title'] + '</td>'
        html += '<td>' + result['artist'] + '</td>'
        html += '<td>' + result['year'] + '</td>'
        html += '<td><img src="https://awsmusicdata.s3.amazonaws.com/' + result['title'] + '.jpg' + '"></img></td>'
        # html += '<td><a href="{{ url_for(\'subscribe\',title=' + result['title'] + ') }}">Subscribe</a></td>'
        html += '<td><a href="{{ url_for(\'subscribe\') }}">Subscribe</a></td>'
        html += '</tr>'
    if results == []:
        flash('No result is retrieved. Please query again.')
    return render_template("home.html", user_name=session["user_name"], queries=Markup(html))



if __name__=='__main__':
    app.run(debug=True)

        # print('THIS HERE:',result['title'])
        # html += '<td><a href="{{ url_for(\'subscribe\', title=result[\'title\']) }}">Subscribe</a></td>'
        # html += '<td><a href="{{ url_for(\'subscribe\', title=result[\'title\']) }}">Subscribe</a></td>'