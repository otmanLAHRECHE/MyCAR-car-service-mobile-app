from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver

from .models import User as User2


@receiver(post_save, sender=User)
def create_profile(sender, instance, created, **kwargs):
    if created:
        User2.objects.create(user=instance)
