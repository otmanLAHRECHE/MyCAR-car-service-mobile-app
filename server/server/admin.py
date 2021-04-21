from django.contrib import admin

# Register your models here.
from server.models import *

admin.site.register(User)
admin.site.register(Car)
admin.site.register(ServiceCenter)
admin.site.register(EngineOil)
admin.site.register(Change)
admin.site.register(Status)
admin.site.register(Verification)