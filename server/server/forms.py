from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User

class LoginForm(forms.Form):
    username = forms.CharField(
        widget=forms.TextInput(
            attrs={
                "placeholder" : "Username",
                "class": "form-control"
            }
        ))
    password = forms.CharField(
        widget=forms.PasswordInput(
            attrs={
                "placeholder" : "Password",
                "class": "form-control"
            }
        ))

class AdviceForm(forms.Form):
    image_url = forms.CharField(
        widget=forms.URLInput(
            attrs={
                "placeholder" : "Image url",
                "class": "form-control",
                "type":"url"
            }
        ))
    title = forms.CharField(
        widget=forms.TextInput(
            attrs={
                "placeholder" : "Title",
                "class": "form-control"
            }
        ))
    content = forms.CharField(
        widget=forms.TextInput(
            attrs={
                "placeholder": "Content",
                "class": "form-control"
            }
        ))