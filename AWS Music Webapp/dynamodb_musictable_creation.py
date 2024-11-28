from decimal import Decimal
import json
import boto3

def create_movie_table(dynamodb=None):
    if not dynamodb:
        dynamodb = boto3.resource('dynamodb',
        region_name='us-east-1')
    table = dynamodb.create_table(
    TableName='music',
    KeySchema=[
    {
    'AttributeName': 'artist',
    'KeyType': 'HASH' # Partition key
    },
    {
    'AttributeName': 'title',
    'KeyType': 'RANGE' # Sort key
    }
    ],
    AttributeDefinitions=[
    {
    'AttributeName': 'artist',
    'AttributeType': 'S'
    },
    {
    'AttributeName': 'title',
    'AttributeType': 'S'
    },
    ],
    ProvisionedThroughput={
    'ReadCapacityUnits': 10,
    'WriteCapacityUnits': 10
    }
    )
    return table


def load_music(songs, dynamodb=None):
    if not dynamodb:
        dynamodb = boto3.resource('dynamodb',
        region_name='us-east-1')
    table = dynamodb.Table('music')
    for song in songs:

        year = song['year']
        title = song['title']
        artist = song['artist']
        web_url = song['web_url']
        img_url = song['img_url']
        print("Adding song:", title, year, artist,web_url,img_url)
        table.put_item(Item={
        'year': year,
        'title': title,
        'artist': artist,
        'web_url': web_url,
        'img_url': img_url
    })
        
if __name__ == '__main__':
    # creates table
    # movie_table = create_movie_table() 
    # print("Table status:", movie_table.table_status)

    # loads data
    with open("C:/Users/Mashal/Documents/RMIT/Cloud Computing/Assignment 1/a1.json") as json_file:
        song_list = json.load(json_file, parse_float=Decimal)
    load_music(song_list['songs'])