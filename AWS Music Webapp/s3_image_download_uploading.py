import requests 
import shutil
import json
from decimal import Decimal

import logging
import boto3
from botocore.exceptions import ClientError

def create_bucket(bucket_name, region=None):
    """Create an S3 bucket in a specified region
    If a region is not specified, the bucket is created in the S3 default
    region (us-east-1).
    :param bucket_name: Bucket to create
    :param region: String region to create bucket in, e.g., 'us-west-2'
    :return: True if bucket created, else False
    """
    # Create bucket
    # Code here cited from week 3 lab -  
    # IEEE ref: RMIT, “Exercise 3 - AWS Storage Services (Python and PHP),” in Week 3. 
    try:
        if region is None:
            s3_client = boto3.client('s3')
            s3_client.create_bucket(Bucket=bucket_name)
        else:
            s3_client = boto3.client('s3', region_name=region)
            location = {'LocationConstraint': region}
            s3_client.create_bucket(Bucket=bucket_name,
            CreateBucketConfiguration=location)
    except ClientError as e:
        logging.error(e)
        return False
    return True

def upload_file(file_name, bucket, object_name=None):
    """Upload a file to an S3 bucket
    :param file_name: File to upload
    :param bucket: Bucket to upload to
    :param object_name: S3 object name. If not specified then file_name is
    used
    :return: True if file was uploaded, else False
    """
# If S3 object_name was not specified, use file_name
    if object_name is None:
        object_name = file_name
    # Upload the file
    s3_client = boto3.client('s3')
    try:
        response = s3_client.upload_file(file_name, bucket, object_name)
    except ClientError as e:
        logging.error(e)
        return False
    return True

# 5.2 List existing buckets
# s3 = boto3.client('s3')
# response = s3.list_buckets()
# # Output the bucket names
# print('Existing buckets:')
# for bucket in response['Buckets']:
#     print(f' {bucket["Name"]}')
# file_path = 'C:/Users/Mashal/Documents/RMIT/Cloud Computing/Assignment 1/yes.txt'
# url = 'https://raw.githubusercontent.com/davidpots/songnotes_cms/master/public/images/artists/TheTallestManOnEarth.jpg'

# import os
# os.chdir('C:/Users/Mashal/Documents/RMIT/Cloud Computing/Assignment 1')
# print(os.getcwd())
i = 0

with open("C:/Users/Mashal/Documents/RMIT/Cloud Computing/Assignment 1/a1.json") as json_file:
    song_list = json.load(json_file, parse_float=Decimal)
    for song in song_list['songs']:
        year = song['year']
        title = song['title']
        artist = song['artist']
        web_url = song['web_url']
        img_url = song['img_url']
        print("Song:",i, title, year, artist,web_url,img_url) # For debugging purposes to ensure files are downloaded correctly
        i+=1
        res = requests.get(img_url, stream = True) # retrieves the image
        file_name = title + '.jpg' # saves file name as title
        with open('C:/Users/Mashal/Documents/RMIT/Cloud Computing/Assignment 1/images/'+file_name,'wb') as f: # writes the image to the file in the images directory
            shutil.copyfileobj(res.raw, f)
        upload_file('C:/Users/Mashal/Documents/RMIT/Cloud Computing/Assignment 1/images/'+ file_name, 'awsmusicdata',file_name) # Uploads the file from the images directory, and saves it as file_name in the s3 root directory

# s3.download_file('awsmusicdata','a1.json','a1.json')
# create_bucket('awsmusicdata')
    
