#!/usr/bin/env python
# coding: utf-8

# # Assignment 2: Milestone I Natural Language Processing
# ## Task 2&3
# #### Student Name: Mashal Khan
# #### Student ID: 3906303
# 
# Date: XXXX
# 
# Version: 1.0
# 
# Environment: Python 3 and Jupyter notebook
# 
# Libraries used: please include all the libraries you used in your assignment, e.g.,:
# * pandas
# * re
# * numpy
# * nltk
# * itertools
# 
# ## Introduction
# You should give a brief information of this assessment task here.
# 
# <span style="color: red"> Note that this is a sample notebook only. You will need to fill in the proper markdown and code blocks. You might also want to make necessary changes to the structure to meet your own needs. Note also that any generic comments written in this notebook are to be removed and replace with your own words.</span>

# Here we the default seed for random functions

# In[1]:


seed = 0


# ## Importing libraries 

# In[2]:


# Code to import libraries as you need in this assessment, e.g.,
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import re
import os
import nltk
from __future__ import division
from itertools import chain
from nltk.probability import FreqDist
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import KFold

from sklearn.manifold import TSNE

from gensim.models.fasttext import FastText


# ## Task 2. Generating Feature Representations for Job Advertisement Descriptions

# ...... Sections and code blocks on buidling different document feature represetations
# 
# 
# <span style="color: red"> You might have complex notebook structure in this section, please feel free to create your own notebook structure. </span>

# #### Reading data from the files

# In[3]:


# Code to perform the task...
descriptions = []
vocab = []
with open('tokenised_descriptions.txt','r') as f:
    for desc in f.readlines():
        descriptions.append(desc.replace('\n',''))
with open('vocab.txt','r') as f:
    for word in f.readlines():
        seg = word.split(':')
        vocab.append((seg[0],seg[1].replace('\n','')))


# In[4]:


i = 0
titles = []
wid = []
companies = []
job_ids=[]
job_txts = []
with open('extra.tsv','r',encoding="utf-8") as f:
    for job in f.readlines():
        chunk = job.split('\t')
        job_ids.append(chunk[0])
        titles.append(chunk[1])
        wid.append(chunk[2])
        companies.append(chunk[3])
        job_txts.append(chunk[4])


# Here we cross check with the real file to verify the correct ordering:

# In[5]:


job_txts[422]


# We use this method stats_print() to verify the data was transported correctly from the task 1 notebook file.

# In[6]:


def stats_print(tokenised_jobs):
    words = list(chain.from_iterable(tokenised_jobs)) # use chain to flatten or put all the tokens in the corpus in a single list
    vocab = set(words) # create the set of types for the tokens i.e the vocabulary by applying the set method to get rid of duplicates.
    lexical_diversity = len(vocab)/len(words) # proportion of uniwue words to all words; how diverse all of the tokens are in the dataset
    print("Vocabulary size: ",len(vocab))
    print("Total number of tokens: ", len(words))
    print("Lexical diversity: ", lexical_diversity)
    print("Total number of articles:", len(tokenised_jobs))
    lens = [len(job) for job in tokenised_jobs]
    print("Average document length:", np.mean(lens))
    print("Maximum document length:", np.max(lens))
    print("Minimum document length:", np.min(lens))
    print("Standard deviation of document length:", np.std(lens))


# We can see from the stats_print output below that we read the data correctly from the file.

# In[7]:


tokenised_jobs = [desc.split() for desc in descriptions]  # list comprehension, generate a list of tokenized job descriptions
stats_print(tokenised_jobs)


# Here we set up the tokens and vocabulary for later use:

# In[8]:


words = list(chain.from_iterable(tokenised_jobs)) # use chain to flatten or put all the tokens in the corpus in a single list
vocab2 = set(words) # We create a voccabulary from the tokenised jobs, 
# to verify that the vocabulary saved to the file inside the vocab variable is correct.


# In[9]:


vocab_set = set([word[0] for word in vocab])

set(sorted(vocab2)) - set([word[0] for word in vocab]) # Checks if the vocabulary matches


# ## Count Vector creation
# Here we create the count vector for all documents in the corpus

# In[10]:


count_vectorizer = CountVectorizer(vocabulary = vocab_set) # initialise the CountVectorizer
count_matrix = count_vectorizer.fit_transform([' '.join(desc) for desc in tokenised_jobs])


# We use the below method from the activity 3 notebook to verify whether our count vectorization is correct. We test with a random sample.

# In[11]:


def validator(data_features, vocab, a_ind, job_ids,job_txts,tokenised_jobs):
    print("Job ID:", job_ids[a_ind]) # print out the Article ID
    print("--------------------------------------------")
    print("Job txt:",job_txts[a_ind]) # print out the txt of the article
    print("Job tokens:",tokenised_jobs[a_ind]) # print out the tokens of the article
    print("--------------------------------------------\n")
    print("Vector representation:\n") # printing the vector representation as format 'word:value' (
                                      # the value is 0 or 1 in for binary vector; an integer for count vector; and a float value for tfidf

    for word, value in zip(vocab, data_features.toarray()[a_ind]): 
        if value > 0:
            print(word+":"+str(value), end =' ')


# In[12]:


test_ind = 29
vocab_set = sorted(list(vocab_set)) # Converts back to sorted list of tokens
validator(count_matrix,vocab_set,test_ind,job_ids,job_txts,tokenised_jobs)


# We can see the frequency-value pairs are correct from the code below, as it matches the output above.

# In[13]:


print(FreqDist(tokenised_jobs[test_ind]).most_common())


# ###### We convert the vectors to a dataframe to use for later.

# In[14]:


# Convert count vector sparse matrix to dataframe
count_df = pd.DataFrame(count_matrix.toarray().transpose(),
                   index=count_vectorizer.get_feature_names())
count_df[45].sort_values(ascending=False)


# ## TF-IDF vector creation
# Same process for TF-IDF vectorization... We will use these objects later for our word-embedding and document vectorization.

# In[15]:


tfidf_vectorizer = TfidfVectorizer(vocabulary = vocab_set) # initialise the TfidfVectorizer
tfidf_matrix = tfidf_vectorizer.fit_transform([' '.join(desc) for desc in tokenised_jobs])


# In[16]:


tfidf_df = pd.DataFrame(tfidf_matrix.toarray().transpose(),
                   index=tfidf_vectorizer.get_feature_names())
tfidf_df[45].sort_values(ascending=False)


# We can see rows represent the vocabulary and columns are the document indices.

# In[17]:


tfidf_df


# ## FastText word embedding

# In[18]:


ft_model = FastText(vector_size=100)  # instantiate the FastText model and vector size of 100
ft_model.build_vocab(corpus_iterable=tokenised_jobs) # Use the cleaned, tokenised documents to pass to corpus_iterable
ft_model.train(corpus_iterable=tokenised_jobs,epochs=ft_model.epochs, # epochs refers to the number of times an example of word is seen (default is 5)
    total_examples=ft_model.corpus_count, total_words=ft_model.corpus_total_words) 
print(ft_model)


# In[19]:


ft_model_wv = ft_model.wv
print('health' in ft_model_wv.key_to_index) # Check if text-embedding is correct


# In[20]:


len(vocab_set)


# In[21]:


print(ft_model_wv.similarity("health", "healthcare")) # Further testing of model accuracy...

print(ft_model_wv.most_similar("healthcare", topn=10))


# In[22]:


# bad = []
# set(set(sorted(vocab_set))- set(sorted(list(ft_model_wv.key_to_index))))==set(bad)


# ### Creating the document vectors for all job descriptions

# In[23]:


def gen_docVecs(wv,tk_txts,tfidf=pd.DataFrame()): # generate vector representation for documents
    docs_vectors = pd.DataFrame() # store all the word embeddings of the article

    for i in range(len(tk_txts)): # loops through all documents
        tokens = list(set(tk_txts[i])) # get the list of distinct words of the document

        temp = pd.DataFrame()  # Creates dataframe to be used. This empties/resets for every new document
        for w_ind in range(len(tokens)): # looping through each word of a single document and spliting through space
            word = tokens[w_ind] # Sets the current word from the documents vocabulary
            word_vec = wv[word] # if word is present in embeddings(goole provides weights associate with words(300)) then proceed
#             if word in tk_txts[i] and word not in ft_model_wv.key_to_index: # for debugging
#                 bad.append(word)
            if not tfidf.empty:
                word_weight = float(list(tfidf.loc[tfidf.index.str.fullmatch(word)][i])[0]) # As the dataframe has strings as indices, it will iterate using the word and i values. The list() and [0] are used to retrieve the actual value
            else:
                word_weight = 1
            temp = temp.append(pd.Series(word_vec*word_weight), ignore_index = True) # if word is present then append it to temporary dataframe

        doc_vector = temp.sum() # take the sum of each column(w0, w1, w2,........w300) which is used to calculate the document vector value
        docs_vectors = docs_vectors.append(doc_vector, ignore_index = True) # append each document value to the final dataframe
    return docs_vectors

weighted_docvecs = gen_docVecs(ft_model_wv,tokenised_jobs,tfidf_df)
unweighted_docvecs = gen_docVecs(ft_model_wv,tokenised_jobs)


# In[24]:


weighted_docvecs


# In[25]:


unweighted_docvecs


# ## Task 3. Job Advertisement Classification

# ...... Sections and code blocks on buidling classification models based on different document feature represetations. 
# Detailed comparsions and evaluations on different models to answer each question as per specification. 
# 
# <span style="color: red"> You might have complex notebook structure in this section, please feel free to create your own notebook structure. </span>

# Here we add the labels to the IDs:

# In[26]:


labels = np.concatenate([np.full(232, 'Engineering'), # This labels each data sample as the category they were found in the file.
                         np.full(191, 'Healthcare'), # The order hasn't changed since reading the files in, 
                         np.full(198, 'Accounting'), # so the correct categories will match the data
                         np.full(155, 'Sales')])


# ### Here we fit the count-based vector and plot the results

# In[27]:


# Here we split the count_vectorizer data into training and testing datasets
X_train, X_test, y_train, y_test,train_indices,test_indices = train_test_split(count_matrix, labels, list(range(0,len(labels))),test_size=0.33, random_state=seed)

# Here we initialize the model
model = LogisticRegression(max_iter = 2000,random_state=seed)
model.fit(X_train, y_train) # Fit the model to the training data
print("Accuracy: ", model.score(X_test, y_test))
print('\n\nHeatmap for count-based vectors logistic regression prediction')

y_pred = model.predict(X_test) # Stores the predicted values

conf_mat = confusion_matrix(y_test, y_pred)
categories = sorted(list(set(labels))) # this gives sorted set of unique label names

sns.heatmap(conf_mat, annot=True, fmt='d',
            xticklabels=categories, yticklabels=categories) # creates a heatmap from the confusion matrix
plt.ylabel('Actual')
plt.xlabel('Predicted')


# ### Here we fit the weighted document vector and plot the results

# In[28]:


def plotTSNE(labels,features): # features as a numpy array, each element of the array is the document embedding of an article
    categories = sorted(list(set(labels)))
    # Sampling a subset of our dataset because t-SNE is computationally expensive
    SAMPLE_SIZE = int(len(features) * 0.3)
    np.random.seed(0)
    indices = np.random.choice(range(len(features)), size=SAMPLE_SIZE, replace=False)
    projected_features = TSNE(n_components=2, random_state=0).fit_transform(features[indices])
    colors = ['pink', 'green', 'midnightblue', 'orange']
    for i in range(0,len(categories)):
        points = projected_features[(labels[indices] == categories[i])]
        plt.scatter(points[:, 0], points[:, 1], s=30, c=colors[i], label=categories[i])
    plt.title("Feature vector for each job description, projected on 2 dimensions.",
              fontdict=dict(fontsize=15))
    plt.legend()
    plt.show()


# In[29]:


features = weighted_docvecs.to_numpy() # convert the dataframe stored features to an numpy array
print("Weighted In-house FastText: tSNE 2 dimensional projected Feature space")
plotTSNE(labels,features)

# creating training and test split
X_train, X_test, y_train, y_test,train_indices,test_indices = train_test_split(weighted_docvecs, labels, list(range(0,len(labels))),test_size=0.33, random_state=seed)

model = LogisticRegression(max_iter = 2000,random_state=seed)
model.fit(X_train, y_train)
print("Accuracy: ", model.score(X_test, y_test))
print('\n\n')


# ### Here we fit the unweighted document vector and plot the results
# 

# In[30]:


features = unweighted_docvecs.to_numpy() # convert the dataframe stored features to an numpy array
print("Unweighted In-house FastText: tSNE 2 dimensional projected Feature space")
plotTSNE(labels,features)
X_train, X_test, y_train, y_test,train_indices,test_indices = train_test_split(unweighted_docvecs, labels, list(range(0,len(labels))),test_size=0.33, random_state=seed)

model = LogisticRegression(max_iter = 2000,random_state=seed)
model.fit(X_train, y_train)
print("Accuracy: ", model.score(X_test, y_test))
print('\n\n')


# ### Implementing 5 K-folds validation

# In[31]:


kf = KFold(n_splits=5) # specifies number of splits, to generate sample indices
def perf_kfolds(wv,labels,count=False):
    average = 0 # Stores average score
    for train_index, test_index in kf.split(wv):
#         print(train_index)
        if count:
            X_train, X_test = wv[train_index], wv[test_index] # Count vector data is in array format rather than dataframe
        else:
            X_train, X_test = wv.iloc[train_index], wv.iloc[test_index]
        y_train, y_test = labels[train_index], labels[test_index]
        model = LogisticRegression(max_iter = 2000,random_state=seed)
        model.fit(X_train, y_train)
        average += model.score(X_test, y_test)
        print("Accuracy: ", model.score(X_test, y_test))
    print('Average:',average/5)


# Here is the weighted TF-IDF k-folds validation:

# In[32]:


perf_kfolds(weighted_docvecs,labels)


# Here is the weighted unweighted k-folds validation:

# In[33]:


perf_kfolds(unweighted_docvecs,labels)


# Here is the count-based k-folds validation:

# In[34]:


perf_kfolds(count_matrix,labels,True)


# ## Using the title column and measuring performance
# We will see if the title feature of all jobs improve the logistic regressor model performance.

# In[36]:


def descTokenizer(text): # bring method from task 1 for tokenisation
    text = text.lower() # convert to lower case
    pattern = r"[a-zA-Z]+(?:[-'][a-zA-Z]+)?" # matches all words, including words with hyphens in between
    tokenizer = nltk.RegexpTokenizer(pattern) 
    tokenised_job = tokenizer.tokenize(text) # tokenises the input text data
    return tokenised_job
tokenised_titles = [descTokenizer(title) for title in titles]


# In[37]:


twords = list(chain.from_iterable(tokenised_titles)) # use chain to flatten or put all the tokens in the corpus in a single list
tvocab = set(twords) # We create a voccabulary from the tokenised jobs, 


# Here we initalize a new TF-IDF vector for titles:

# In[38]:


title_tfidf_vectorizer = TfidfVectorizer(vocabulary = tvocab) # initialise the TfidfVectorizer
title_matrix = tfidf_vectorizer.fit_transform([' '.join(desc) for desc in tokenised_jobs])


# ### Q1: It seems the normal counts vectoriser is the best model, rather than the word-embedding models.

# ### Saving outputs
# Save the count vector representation as per spectification.
# - count_vectors.txt

# In[41]:


count_df


# In[46]:


# code to save output data...
with open('count_vectors.txt','w') as f:
    for i in range(len(wid)):
        f.write('#' + str(wid[i])+',')
        for ind,row in count_df.iterrows():
            f.write(str(row[i])+':'+ind)
        f.write('\n')


# ## Summary
# Unfortunately, I scored a very low accuracy score for the weighted TF-IDF word embedding model, of 54%. One potential reason is that when using the gen_docVecs() method, as it was retrieving the words from the FastText object, it was not recognising words from the tokenised_words vocabulary. Although this could be an intentional effect of the object.

# ## Couple of notes for all code blocks in this notebook
# - please provide proper comment on your code
# - Please re-start and run all cells to make sure codes are runable and include your output in the submission.   
# <span style="color: red"> This markdown block can be removed once the task is completed. </span>
