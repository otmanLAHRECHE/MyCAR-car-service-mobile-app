from rest_framework import permissions


# object level permission
from rest_framework.permissions import SAFE_METHODS


class IsOwner(permissions.BasePermission):
    """
    Object-level permission to only allow owners of an object to edit it.
    Assumes the model instance has an `username_id` attribute, which denotes owner's id.
    """

    def has_object_permission(self, request, view, obj):
        if request.method in SAFE_METHODS:
            return True
        return obj.owner == request.user


