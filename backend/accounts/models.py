from django.contrib.auth.models import AbstractBaseUser, BaseUserManager, PermissionsMixin
from django.db import models
import uuid
from django.contrib.auth.hashers import make_password
from django.http import Http404, HttpResponseNotFound, HttpResponseServerError

# class UserManager(BaseUserManager):
#     def create_user(self, username, full_name, password=None):
#         user = self.model(
#             username=username,
#             full_name=full_name,
#         )

#         user.set_password(password)
#         user.save(using=self._db)

#         return user

#     def create_superuser(self, username, full_name, password):
#         user = self.create_user(
#             username=username,
#             full_name=full_name,
#             password=password,
#         )
#         user.is_staff = True
#         user.is_superuser = True
#         user.save(using=self._db)

#         return user


# class User(AbstractBaseUser, PermissionsMixin):
#     username = models.CharField(max_length=30, unique=True)
#     # email = models.EmailField(max_length=255, unique=True)
#     full_name = models.CharField(max_length=255)
#     is_active = models.BooleanField(default=True)
#     is_staff = models.BooleanField(default=False)
#     date_joined = models.DateTimeField(auto_now_add=True)

#     objects = UserManager()

#     USERNAME_FIELD = 'username'
#     REQUIRED_FIELDS = ['full_name']

#     def __str__(self):
#         return self.username

#     def get_full_name(self):
#         return self.full_name

#     def get_short_name(self):
#         return self.username
    
class User(AbstractBaseUser):
    id = models.CharField(
        max_length=36, default=uuid.uuid4, primary_key=True, unique=True
    )
    full_name = models.CharField(max_length=100)
    email = models.EmailField(max_length=100, null=True, blank=True)
    date_created = models.DateTimeField(auto_now_add=True)
    

    USERNAME_FIELD = "id"

    class Meta:
        verbose_name = "User"
        verbose_name_plural = "Users"

    def __str__(self):
        return str(self.user_id)

    def save(self) -> None:
        self.password = make_password(password=self.password)
        return super().save()

    @classmethod
    def get_by_id(cls, user_id):
        try:
            return cls.objects.get(user_id=user_id)
        except (Http404, cls.DoesNotExist) as e:
            raise HttpResponseNotFound("No User found with the given information.")
        except Exception as e:
            raise HttpResponseServerError("Unable to fetch user details.", errors=str(e))

    @classmethod
    def get_by_email(cls, email):
        try:
            return cls.objects.get(email=email)
        except (Http404, cls.DoesNotExist) as e:
            raise HttpResponseNotFound("No User found with the given information.")
        except Exception as e:
            raise HttpResponseServerError("Unable to fetch user details.", errors=str(e))

class Journal(models.Model):
    id = models.CharField(
        max_length=36, default=uuid.uuid4, primary_key=True, unique=True
    )
    username = models.CharField(max_length=30)
    entry = models.TextField()
    date = models.CharField(max_length=50)
    label = models.CharField(max_length=30)