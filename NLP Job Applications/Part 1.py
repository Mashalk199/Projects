#!/usr/bin/env python
# coding: utf-8

# # Assignment 2: Milestone I Natural Language Processing
# ## Task 1. Basic Text Pre-processing
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

# ## Importing libraries 

# In[1]:


# Code to import libraries as you need in this assessment, e.g.,
import numpy as np
import pandas as pd
import re
import os
import nltk
from itertools import chain
from nltk.probability import FreqDist


# ### 1.1 Examining and loading data
# - xamine the data folder, including the categories and job advertisment txt documents, etc. Explain your findings here, e.g., number of folders and format of txt files, etc.
# - Load the data into proper data structures and get it ready for processing.
# - Extract webIndex and description into proper data structures.
# 

# We're going to check the files structure of the data folder. Then check the contents of their files, to understand the data and its format.

# In[2]:


dir_path = "data/Engineering"
print(os.listdir(dir_path)[:5]) # os.listdir() retrieves all the file names in a directory
dir_path = "data/Healthcare_Nursing"
print(os.listdir(dir_path)[:5])
dir_path = "data/Accounting_Finance"
print(os.listdir(dir_path)[:5])
dir_path = "data/Sales"
print(os.listdir(dir_path)[:5])


# Let's read the contents of a file:

# In[3]:


path = os.path.join(dir_path,'Job_00623.txt') # this gives the file path, e.g., './Engineering/Job_0001.txt'
with open(path,"r",encoding= 'unicode_escape') as f: # open the txt file
    print('Contents:\n',f.read())


# Let's read the data in from the files. We will read them in from the order of job IDs from the file structure:

# In[4]:


# Code to inspect the provided data file...
dir_path = "data/Engineering"
job_ids = [] # list to store the job ID
job_txts = [] # list to store the raw text

for filename in sorted(os.listdir(dir_path)): # we want to load articles in ascending order of their file names
    if filename.endswith(".txt"): # we only look at the txt file
        job_ids.append(filename.split(".")[0][4:]) # split the file name with '.', 
                                                    # so the first part is the article ID, and 2nd part is 'txt'
                                                    # we then take the first part and store it
                                                    # and start from the 4th character, to just store the number
        path = os.path.join(dir_path,filename) # this gives the file path, e.g., './Engineering/Job_0001.txt'
        with open(path,"r",encoding= 'utf-8') as f: # open the txt file
            job_txts.append(f.read()) # read the file into a string, and append it to the article_txts list
            f.close()
dir_path = "data/Accounting_Finance"

for filename in sorted(os.listdir(dir_path)): # we want to load articles in ascending order of their file names
    if filename.endswith(".txt"): # we only look at the txt file
        job_ids.append(filename.split(".")[0][4:]) # split the file name with '.', 
                                                    # so the first part is the article ID, and 2nd part is 'txt'
                                                    # we then take the first part and store it
                                                    # and start from the 4th character, to just store the number
        path = os.path.join(dir_path,filename) # this gives the file path, e.g., './Engineering/Job_0001.txt'
        with open(path,"r",encoding= 'utf-8') as f: # open the txt file
            job_txts.append(f.read()) # read the file into a string, and append it to the article_txts list
            f.close()
dir_path = "data/Healthcare_Nursing"

for filename in sorted(os.listdir(dir_path)): # we want to load articles in ascending order of their file names
    if filename.endswith(".txt"): # we only look at the txt file
        job_ids.append(filename.split(".")[0][4:]) # split the file name with '.', 
                                                    # so the first part is the article ID, and 2nd part is 'txt'
                                                    # we then take the first part and store it
                                                    # and start from the 4th character, to just store the number
        path = os.path.join(dir_path,filename) # this gives the file path, e.g., './Engineering/Job_0001.txt'
        with open(path,"r",encoding= 'utf-8') as f: # open the txt file
            job_txts.append(f.read()) # read the file into a string, and append it to the article_txts list
            f.close()
dir_path = 'data/Sales'
for filename in sorted(os.listdir(dir_path)): # we want to load articles in ascending order of their file names
    if filename.endswith(".txt"): # we only look at the txt file
        job_ids.append(filename.split(".")[0][4:]) # split the file name with '.', 
                                                    # so the first part is the article ID, and 2nd part is 'txt'
                                                    # we then take the first part and store it
                                                    # and start from the 4th character, to just store the number
        path = os.path.join(dir_path,filename) # this gives the file path, e.g., './Engineering/Job_0001.txt'
        with open(path,"r",encoding= 'utf-8') as f: # open the txt file
            job_txts.append(f.read()) # read the file into a string, and append it to the article_txts list
            f.close()


# Let's check if we have read them in correctly

# In[5]:


print("number of article txt read:", len(job_txts))
print("number of article IDs read:", len(job_ids))


# This matches the number of the last ID in the Sales folder, so data has been read correctly.
# 
# Let's extract the pieces of data from all job data entries, and store them in lists.

# In[6]:


# Here we create regex objects to separately extract attributes from the text files. (Makes for cleaner code)
regTitle = re.compile(r'Title: *(.+)')
regWebind = re.compile(r'Webindex: *(\d+)')
regComp = re.compile(r'Company: *(.+)')
regDesc = re.compile(r'Description: *(.+)')
titles = []
wid = []
companies = []
descriptions = []

for file in job_txts:
    titles.append(regTitle.match(file).group(1) if regTitle.search(file) else 'None')
    wid.append(regWebind.search(file).group(1) if regWebind.search(file) else 'None')
    companies.append(regComp.search(file).group(1) if regComp.search(file) else 'None')
    descriptions.append(regDesc.search(file).group(1) if regDesc.search(file) else 'None')


# Let's verify the data has been read correctly:

# In[7]:


print(len(titles))
print(titles[:5])
print(len(wid))
print(wid[:5])
print(len(companies))
print(companies[:5])
print(len(descriptions))
print(descriptions[:5])


# It appears all of the data has been parsed correctly, when cross-checking with the actual txt files. All of th data has a length of 767 which is correct.

# In[8]:


test_ind = 399 # randomly select an element to check wehter the article ID and txt are correctly correspond to each other, 
original_txts = descriptions # Will save these for writing to file at the end
print("Job ID:", job_ids[test_ind])
print("Job txt:\n", job_txts[test_ind])


# ### 1.2 Pre-processing data
# Perform the required text pre-processing steps.

# ...... Sections and code blocks on basic text pre-processing
# 
# 
# <span style="color: red"> You might have complex notebook structure in this section, please feel free to create your own notebook structure. </span>

# We tokenise the job descriptions across all job listings using this method and the provided regex.

# In[9]:


# code to perform the task...
def descTokenizer(text):
    text = text.lower() # convert to lower case
    pattern = r"[a-zA-Z]+(?:[-'][a-zA-Z]+)?" # matches all words, including words with hyphens in between
    tokenizer = nltk.RegexpTokenizer(pattern) 
    tokenised_job = tokenizer.tokenize(text) # tokenises the input text data
    return tokenised_job


# In[10]:


# code to save output data...
tokenised_jobs = [descTokenizer(desc) for desc in descriptions]  # list comprehension, generate a list of tokenized job descriptions


# In[11]:


tokenised_jobs


# This method conveniently prints out the textual statistics of the corpus, using the tokenised job descriptions

# In[12]:


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


# In[13]:


stats_print(tokenised_jobs)


# In[14]:


words = list(chain.from_iterable(tokenised_jobs)) # we put all the tokens in the corpus in a single list
vocab = set(words) # compute the vocabulary by converting the list of words/tokens to a set, i.e., giving a set of unique words


# In[15]:


term_fd = FreqDist(words) # compute term frequency for each unique word/type
term_fd.most_common(25)


# ## Cleaning the tokens
# ### Remove all tokens with less than 2 characters

# In[16]:


tokenised_jobs = [[word for word in desc if len(word) > 1] # Generate a subset of all tokens with lengths greater than 1
                      for desc in tokenised_jobs]


# ### Removing stopwords

# In[17]:


stop_words = []
with open('stopwords_en.txt','r') as f: # Read stopwords from file
    for ln in f.readlines():
        stop_words.append(ln.rstrip('\n')) # ln.strip removes '\n' from the strings
        
tokenised_jobs = [[w for w in desc if w not in stop_words] # return list with all the words NOT in the stop_words list
                      for desc in tokenised_jobs]
words = list(chain.from_iterable([set(desc) for desc in tokenised_jobs]))
doc_fd = FreqDist(words)
doc_fd.most_common(25)


# ### Removing lowest frequency words across all documents

# In[18]:


lessFreqWords = set(doc_fd.hapaxes()) # Returns list of all word tokens with a frequency of 1
lessFreqWords


# In[19]:


tokenised_jobs = [[w for w in desc if w not in lessFreqWords] # return list with all the words with a frequency of 1 in a job description
                      for desc in tokenised_jobs]
words = list(chain.from_iterable([set(desc) for desc in tokenised_jobs]))
doc_fd = FreqDist(words)
doc_fd.most_common(25)


# ###  Remove the top 50 words from the corpus

# In[20]:


top_50 = [word[0] for word in doc_fd.most_common(50)] # We store the top 50 words to verify their removal later
tokenised_jobs = [[w for w in desc if w not in top_50] # return list with all the top 50 common words in a job description
                      for desc in tokenised_jobs]
words = list(chain.from_iterable([set(desc) for desc in tokenised_jobs]))
doc_fd = FreqDist(words)
doc_fd.most_common(50)


# Verify all top 50 words have been removed from corpus:

# In[21]:


for l in [[w for w in desc if w in top_50] for desc in tokenised_jobs]:
    if l!=[]:
        print(l)
print('Done')


# In[22]:


doc_fd.plot(25, cumulative=True) # Check the frequency distribution across the corpus


# We use the stats_print() method to evaluate the difference we made in the preprocessing of the tokenisation and vocabulary.
# 
# We've dramatically reduced the size of the vocabulary and documents/tokens, however the lexical diversity has not improved much.

# In[24]:


stats_print(tokenised_jobs)


# Original:<br>
# Vocabulary size:  9834<br>
# Total number of tokens:  186952<br>
# Lexical diversity:  0.052601737344345076<br>
# Total number of articles: 776<br>
# Average document length: 240.91752577319588<br>
# Maximum document length: 815<br>
# Minimum document length: 13<br>
# Standard deviation of document length: 124.97750685071483<br>

# ## Saving required outputs
# Save the vocabulary, bigrams and job advertisment txt as per spectification.
# - vocab.txt

# In[25]:


words = list(chain.from_iterable([set(desc) for desc in tokenised_jobs]))

[word for word in words if '\n' in word] # Verify no /n characters are in the data


# In[26]:


print(doc_fd)


# In[27]:


doc_fd = FreqDist(words)
#This code sorts the vocabulary by the frequency of word occurrences, using a lambda function
sorted_freqs = sorted([(word[0],word[1]) for word in doc_fd.most_common()],key=lambda x:x[0]) 


# In[28]:


sorted_freqs


# In[30]:


with open('tokenised_descriptions.txt','w') as f:
    f.writelines([(' '.join(desc) + '\n') for desc in tokenised_jobs])
    
with open('vocab.txt','w') as f:
    for pair in sorted_freqs:
        f.write(pair[0] + ':' + str(pair[1]) + '\n')


# In[31]:


'\t'.join([job_ids[44],titles[44],wid[44],companies[44]]) # Verify the file data is split up by tabs


# In[32]:


[title for title in titles if '\n' in title]


# In[33]:


with open('job_txts.txt','w',encoding="utf-8") as f:
    f.writelines([desc + '\n' for desc in job_txts])

i = 0
with open('extra.tsv','w',encoding="utf-8") as f:
    for job_id in job_ids:
        f.write('\t'.join([job_id,titles[i],wid[i],companies[i],original_txts[i]]) + '\n')
        i += 1


# ## Summary
# The data has been successfully tokenized and preprocessed.
