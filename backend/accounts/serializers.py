from rest_framework import serializers
from .models import User, Journal


# class UserSerializer(serializers.ModelSerializer):
#     password = serializers.CharField(write_only=True)

#     class Meta:
#         model = User
#         fields = ('id', 'username', 'full_name', 'password')
#         read_only_fields = ('id',)

    # def create(self, validated_data):
    #     user = User.objects.create_user(**validated_data)
    #     return user

class VerificationUserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)
    class Meta:
        model = User
        fields = ('id', 'full_name', 'email', 'password')
        read_only_fields = ('id',)
        exclude = ("password")

class JournalSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Journal
        fields = ('id', 'username', 'entry', 'date', 'label')
        read_only_fields = ('id',)