import uuid
from random import randint

from django.db.models import Count
from django.utils import timezone

from django.conf import settings
from django.contrib.auth.base_user import BaseUserManager, AbstractBaseUser
from django.contrib.auth.models import User, AbstractUser, PermissionsMixin
from django.db import models
from django.db.models.signals import post_save
from django.dispatch import receiver
from rest_framework.authtoken.models import Token
import json

class UserManager(BaseUserManager):
    def _create_user(self, email, password, is_staff,is_active, is_superuser, **extra_fields):
        if not email:
            raise ValueError('Users must have an email address')
        now = timezone.now()
        email = self.normalize_email(email)
        user = self.model(
            email=email,
            is_staff=is_staff,
            is_active=is_active,
            is_superuser=is_superuser,
            last_login=now,
            date_joined=now,
            **extra_fields
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_user(self, email, password, **extra_fields):
        return self._create_user(email, password, False,False, False, **extra_fields)

    def create_superuser(self, email, password, **extra_fields):
        user = self._create_user(email, password, True,True, True, **extra_fields)
        user.save(using=self._db)
        return user


class User(AbstractBaseUser, PermissionsMixin):
    first_name = models.CharField(max_length=254, null=True, blank=True)
    last_name = models.CharField(max_length=254, null=True, blank=True)
    email = models.EmailField(max_length=254, unique=True)
    is_staff = models.BooleanField(default=False)
    is_superuser = models.BooleanField(default=False)
    is_active = models.BooleanField(default=True)
    last_login = models.DateTimeField(null=True, blank=True)
    date_joined = models.DateTimeField(auto_now_add=True)

    USERNAME_FIELD = 'email'
    EMAIL_FIELD = 'email'
    REQUIRED_FIELDS = ['first_name','last_name']

    objects = UserManager()

    def get_absolute_url(self):
        return "/users/%i/" % (self.pk)




class Car(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4,editable=False)
    owner = models.ForeignKey(User,on_delete=models.CASCADE,related_name='cars')
    company = models.CharField(max_length=15)
    model = models.CharField(max_length=15)
    year = models.IntegerField(max_length=10)
    car_type = models.CharField(max_length=15)
    serial = models.CharField(max_length=20)
    registration = models.PositiveIntegerField(max_length=20)

    def __str__(self):
        return self.company


class ServiceCenter(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4(),editable=False)
    owner = models.ForeignKey(User,on_delete=models.CASCADE,related_name='service_centers')
    name = models.CharField(max_length=15)
    location = models.CharField(max_length=20)
    phoneNumber = models.PositiveIntegerField(20)

    def __str__(self):
        return self.name

class EngineOil(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4(),editable=False)
    nextOdometer = models.PositiveIntegerField(max_length=20)
    type = models.CharField(max_length=15)

    def __str__(self):return self.type


class Change(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4(),editable=False)
    oil_filter = models.BooleanField()
    air_filter = models.BooleanField()
    cabine_filter = models.BooleanField()
    light_replace = models.BooleanField()
    wheel_alignment = models.BooleanField()
    brakes = models.BooleanField()
    transmission_oil = models.BooleanField()
    battery_replace = models.BooleanField()
    tires_change = models.BooleanField()
    fuel_filter_change = models.BooleanField()
    glass_change = models.BooleanField()
    mount_and_balance = models.BooleanField()

    def __str__(self):
        return self.id



class Status(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4(),editable=False)
    engineState = models.CharField(max_length=20)
    lightsState = models.CharField(max_length=20)
    tiresState = models.CharField(max_length=20)
    airConditioningState = models.CharField(max_length=20)

    def __str__(self):return self.id




class Verification(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4(),editable=False)
    date = models.BigIntegerField(max_length=20)
    odometer = models.PositiveIntegerField(max_length=20)
    car = models.ForeignKey(Car,on_delete=models.CASCADE,related_name='verifications')
    service_center = models.ForeignKey(ServiceCenter,on_delete=models.CASCADE,related_name='verifications')
    engine_oil = models.OneToOneField(EngineOil,on_delete=models.CASCADE)
    changes = models.OneToOneField(Change,on_delete=models.CASCADE)
    states = models.OneToOneField(Status,on_delete=models.CASCADE)
    note = models.CharField(max_length=200)

    def __str__(self):return str(self.id)


class Reparation(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4(), editable=False)
    date = models.BigIntegerField(max_length=20)
    odometer = models.PositiveIntegerField(max_length=20)
    car = models.ForeignKey(Car, on_delete=models.CASCADE, related_name='reparations')
    service_center = models.ForeignKey(ServiceCenter, on_delete=models.CASCADE, related_name='reparations')
    what_repared = models.CharField(max_length=200)
    note = models.CharField(max_length=200)

    def __str__(self): return str(self.id)



class PredictedMaintenance(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4(), editable=False)
    date = models.BigIntegerField(max_length=20)
    car = models.ForeignKey(Car, on_delete=models.CASCADE, related_name='predicted_maintenance')
    predictedMaintenance = models.TextField()

    def __str__(self): return str(self.id)

    def set_predictedMaintenance(self,k):
        self.predictedMaintenance = k


    def get_predictedMaintenance(self):
        return json.loads(self.predictedMaintenance)



class Advice(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4(), editable=False)
    title = models.CharField(max_length=200)
    content = models.TextField(max_length=700)
    url = models.CharField(max_length=200)

    def __str__(self): return str(self.id)

    def random(self):
        count = self.aggregate(count=Count('id'))['count']
        random_index = randint(0, count - 1)
        return self.all()[random_index]


class Trade(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4(), editable=False)
    car = models.ForeignKey(Car,on_delete=models.CASCADE,related_name='trades')
    buyer = models.ForeignKey(User,on_delete=models.CASCADE,related_name='buyer')
    seller = models.ForeignKey(User,on_delete=models.CASCADE, related_name='seller')
    date = models.BigIntegerField(max_length=20)

    def __str__(self): return str(self.id)










