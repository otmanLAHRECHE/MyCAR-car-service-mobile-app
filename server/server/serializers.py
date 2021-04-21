from django.contrib.auth.base_user import BaseUserManager, AbstractBaseUser
from django.contrib.auth.models import User
from rest_framework import serializers

from server.models import *



class CarSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    owner_username = serializers.ReadOnlyField(source='owner.username')

    class Meta:
        fields = ('id','company','model','serial','year','car_type','registration','owner_username')
        model = Car

class ServiceCenterSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    owner_username = serializers.ReadOnlyField(source='owner.username')
    class Meta:
        fields = ('id','name','location','phoneNumber','owner_username')
        model = ServiceCenter

class EngineOilSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    class Meta:
        fields = ('id', 'nextOdometer', 'type')
        model = EngineOil


class ChangeSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)


    class Meta:
        fields = ('id', 'oil_filter', 'air_filter','cabine_filter','brakes','transmission_oil','light_replace','wheel_alignment','battery_replace','tires_change','fuel_filter_change','glass_change','mount_and_balance')
        model = Change

class StatusSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    class Meta:
        fields = ('id', 'engineState', 'lightsState', 'tiresState', 'airConditioningState')
        model = Status

class VerificationSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    car = CarSerializer()
    engine_oil = EngineOilSerializer()
    service_center = ServiceCenterSerializer()
    states = StatusSerializer()
    changes = ChangeSerializer()

    class Meta:
        fields = ('id', 'date', 'odometer', 'car', 'service_center','engine_oil','changes','states','note')
        model = Verification



class ReparationSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    car = CarSerializer()
    service_center = ServiceCenterSerializer()

    class Meta:
        fields = ('id', 'date', 'odometer', 'car', 'service_center','what_repared','note')
        model = Reparation



class PredictiveMaintenanceSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    car = CarSerializer()

    class Meta:
        fields = ('id', 'date','car','predictedMaintenance')
        model = PredictedMaintenance


class AdviceSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)

    class Meta:
        fields = ('id', 'title', 'content', 'url')
        model = Advice


class TradeSerializer(serializers.ModelSerializer):
    id = serializers.UUIDField(read_only=False, default=None)
    car = CarSerializer()
    seller_username = serializers.ReadOnlyField(source='buyer.username')
    buyer_username = serializers.ReadOnlyField(source='buyer.username')

    class Meta:
        fields = ('id', 'car','date','buyer','seller_username','buyer_username')
        model = Trade








