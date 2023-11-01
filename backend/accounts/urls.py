from django.urls import path
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView, TokenVerifyView
from .views import Signup, EmoSenseView, InputView, OutputView, OutputAllView, LoginView

urlpatterns = [
    # path('signup/', Signup.as_view(), name='signup'),
    path('signin/', LoginView.as_view(), name='token_obtain_pair'),
    path('signin/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('emosense/', EmoSenseView.as_view(), name='emosense'),
    path('input/', InputView.as_view(), name='input'),
    path('output/', OutputView.as_view(), name='output'),
    path('outputall/', OutputAllView.as_view(), name='outputall'),

    path('verify/', TokenVerifyView.as_view(), name='verify'),
]