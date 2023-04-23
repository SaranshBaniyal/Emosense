from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from .serializers import UserSerializer, JournalSerializer
from django.contrib.auth import authenticate
import datetime
from rest_framework.permissions import AllowAny
from .models import Journal
import os

# from transformers import RobertaTokenizerFast, TFRobertaForSequenceClassification, pipeline
import json
import requests

@api_view(['POST'])
@permission_classes([AllowAny])
def signup(request):
    serializer = UserSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
@permission_classes([AllowAny])
def login(request):
    username = request.data.get('username')
    password = request.data.get('password')
    user = authenticate(request, username=username, password=password)
    if user is not None:
        return Response({'success': True, 'message': 'Login successful'})
    else:
        return Response({'success': False, 'message': 'Invalid credentials'}, status=status.HTTP_401_UNAUTHORIZED)
    
@api_view(['POST'])
@permission_classes([AllowAny])
def input(request):
    request.data._mutable=True #just added to fix AttributeError: This QueryDict instance is immutable

    request.data['date'] = str(datetime.date.today())  # Returns 2018-01-15

    API_TOKEN = "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"
    API_URL = "https://api-inference.huggingface.co/models/arpanghoshal/EmoRoBERTa"
    headers = {"Authorization": f"Bearer {API_TOKEN}"}
    entry = request.data.get('entry')
    data = json.dumps(entry)
    response = requests.request("POST", API_URL, headers=headers, data=data)

    request.data['label'] = response.json()[0][0]['label']
    # request.data['label'] = "test"

    serializer = JournalSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
@permission_classes([AllowAny])
def output(request):
   
    username = request.data.get('username')
    # date = "2023-03-03"
    date = request.data.get('date')

    queryset = Journal.objects.filter(username=username, date=date)

    data = list(queryset.values())
    return Response(data)


@api_view(['POST'])
@permission_classes([AllowAny])
def outputall(request):
    username = request.data.get('username')

    queryset = Journal.objects.filter(username=username).order_by('-date')
    data = list(queryset.values())
    return Response(data)

@api_view(['GET'])
@permission_classes([AllowAny])
def emosense(request):
    API_TOKEN = os.environ.get('HuggingFaceAPIKey')
    API_URL = "https://api-inference.huggingface.co/models/arpanghoshal/EmoRoBERTa"
    headers = {"Authorization": f"Bearer {API_TOKEN}"}
    data = json.dumps("I love winters")
    response = requests.request("POST", API_URL, headers=headers, data=data)

    return Response(response.json()[0][0]['label'])