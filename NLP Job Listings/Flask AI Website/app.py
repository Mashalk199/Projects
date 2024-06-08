from flask import Flask,render_template,request,flash
import numpy as np
import pandas as pd
from markupsafe import Markup
import nltk
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
import random
import pickle

app = Flask(__name__)
app.secret_key = 'bzsjrfbt74sb84rg8w4e8a'
colours = ['red', 'yellow', 'blue', 'green', 'orange',  'purple','pink']

@app.route('/')
def home():
    return render_template('home.html')
@app.route('/job_ad.html')
def job_ad():
    wi = int(request.args.get('wid'))
    data = pd.read_csv('data.csv')
    title =  data.loc[data['wid']==wi,'titles'].item()
    company =  data.loc[data['wid']==wi,'companies'].item()
    description =  data.loc[data['wid']==wi,'descriptions'].item()
    category =  data.loc[data['wid']==wi,'category'].item()

    return render_template('/job_ad.html',title = title,wid=wi,company = company, description = description,category=category)

@app.route('/listings.html')
def listings():
    data = pd.read_csv('data.csv')
    html = ''

    for index, row in data.iterrows():
        html += '<tr style="background-color:' + random.choice(colours) + ';"><td>' + row['titles'] + '</td>'
        html += '<td>'+row['companies'] + '</td>' + '<td>'+row['category'] + '</td>'
        html += '<td><a href =\"job_ad.html?wid=' + str(row['wid']) + '\">Visit<a/></td></tr>'
    return render_template('/listings.html',tdata=Markup(html))

@app.route('/post_job.html',methods=['GET','POST'])
def post_job():
    if request.method == 'POST':
        title=request.form['title']
        company=request.form['company']
        wid=request.form['wid']
        description=request.form['description']
        category=request.form['category']

        data = pd.read_csv('data.csv')

        if request.form['submit'] == 'Classify':
            print('THISSSS:',data['wid'],data[data['wid'].isin([wid])].shape[0]==1,wid,data.dtypes,type(wid))
            if data[data['wid'].isin([wid])].shape[0]==0:


                with open('vocabulary.txt') as file:
                    vocab_set = file.read().splitlines()
                count_vectorizer = CountVectorizer(vocabulary = vocab_set) # initialise the CountVectorizer
                count_matrix = count_vectorizer.fit_transform([description])
                model = pickle.load(open('lrmodel.pkl', 'rb'))


                category = model.predict(count_matrix)[0]

                return render_template('/post_job.html',title=title,company=company,
                wid=wid,description=description,category=category)
            else:
                flash('That WebIndex is already in use. Please try another one.')
                return render_template('/post_job.html',title=title,company=company,
                wid=wid,description=description,category=category)
        elif request.form['submit'] == 'Submit':
            if data[data['wid'].isin([wid])].shape[0]==0:
                if category =='':
                    flash('Job Category can\'t be empty.')
                    return render_template('/post_job.html',title=title,company=company,
                    wid=wid,description=description,category=category)
                else:
                    data = data.append({'titles':title,'companies':company,'wid':wid,'descriptions':description,'category':category}, ignore_index=True)
                    data.to_csv('data.csv')
                    return render_template('/job_ad.html',title = title,wid=wid,company = company, description = description,category=category)
            else:
                flash('That WebIndex is already in use. Please try another one.')
                return render_template('/post_job.html',title=title,company=company,
                wid=wid,description=description,category=category)
    else:
        return render_template('/post_job.html')