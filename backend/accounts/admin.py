# myapp/admin.py

from django.contrib import admin
from .models import User, Journal

# Create an admin class for the User model
class UserAdmin(admin.ModelAdmin):
    list_display = ('id', 'full_name', 'email', 'date_created')
    list_filter = ('is_active', 'is_admin')
    search_fields = ('full_name', 'email')
    ordering = ('-date_created',)
    filter_horizontal = ()
    fieldsets = (
        ('User Info', {'fields': ('full_name', 'email', 'password')}),
        ('Permissions', {'fields': ('is_active', 'is_admin')}),
    )

# Create an admin class for the Journal model
class JournalAdmin(admin.ModelAdmin):
    list_display = ('id', 'user', 'entry', 'date', 'label')
    list_filter = ('user', 'date')
    search_fields = ('entry', 'label')
    ordering = ('-date',)

# Register your models and admin classes
admin.site.register(User, UserAdmin)
admin.site.register(Journal, JournalAdmin)
