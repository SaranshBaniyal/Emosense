
from .serializers import UserSerializer, JournalSerializer
import datetime
from rest_framework.permissions import AllowAny
from .models import Journal
import os
import json
import requests
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.views import TokenObtainPairView
from .serializers import UserSerializer
from rest_framework.permissions import IsAuthenticated
from rest_framework.authentication import TokenAuthentication
from django.contrib.auth import authenticate
from rest_framework_simplejwt.authentication import JWTAuthentication
from rest_framework_simplejwt.tokens import RefreshToken

API_TOKEN = "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"

class Signup(APIView):
    permission_classes = (AllowAny,)

    def post(self, request):
        serializer = UserSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.save()
            if user:
                return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class LoginView(APIView):
    permission_classes = (AllowAny,)
    def post(self, request):
        email = request.data.get('email')
        password = request.data.get('password')
        user = authenticate(request, email=email, password=password)
        
        if user is not None:
            user_serializer = UserSerializer(user)

            # Generate JWT tokens
            refresh = RefreshToken.for_user(user)
            access_token = str(refresh.access_token)

            response_data = {
                'success': True,
                'message': 'Login successful',
                'user': user_serializer.data,
                'access_token': access_token,
                'refresh_token': str(refresh),
            }
            return Response(response_data)
        else:
            return Response({'success': False, 'message': 'Invalid credentials'}, status=status.HTTP_401_UNAUTHORIZED)

class EmoSenseView(APIView):
    # authentication_classes = [TokenAuthentication]
    authentication_classes = [JWTAuthentication]
    permission_classes = [IsAuthenticated]
    # permission_classes = [AllowAny]

    def get(self, request):
        # API_TOKEN = os.environ.get('HuggingFaceAPIKey')
        API_URL = "https://api-inference.huggingface.co/models/arpanghoshal/EmoRoBERTa"
        headers = {"Authorization": f"Bearer {API_TOKEN}"}
        
        data = json.dumps({"inputs": "I love winters"})  # Wrap the input data in a dictionary

        response = requests.post(API_URL, headers=headers, data=data)
        # print("user", request.user.id, request.user.full_name)
        if response.status_code == 200:
            result = response.json()
            # Modify this part based on the actual structure of the API response
            label = result[0][0]['label']
            return Response(label)
        else:
            return Response("Error occurred while making the API request", status=status.HTTP_400_BAD_REQUEST)
        
class InputView(APIView):
    permission_classes = [AllowAny]

    def post(self, request):
        # request.data._mutable = True

        request.data['date'] = str(datetime.date.today())

        API_URL = "https://api-inference.huggingface.co/models/arpanghoshal/EmoRoBERTa"
        headers = {"Authorization": f"Bearer {API_TOKEN}"}
        entry = request.data.get('entry')
        data = json.dumps(entry)
        response = requests.request("POST", API_URL, headers=headers, data=data)

        request.data['label'] = response.json()[0][0]['label']

        serializer = JournalSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class OutputView(APIView):
    permission_classes = [AllowAny]

    def post(self, request):
        user = request.data.get('user')
        date = request.data.get('date')

        queryset = Journal.objects.filter(user=user, date=date)

        data = list(queryset.values())
        return Response(data)

class OutputAllView(APIView):
    permission_classes = [AllowAny]

    def post(self, request):
        user = request.data.get('user')

        queryset = Journal.objects.filter(user=user).order_by('-date')
        data = list(queryset.values())
        return Response(data)
