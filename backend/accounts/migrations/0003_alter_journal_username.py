# Generated by Django 4.1.6 on 2023-03-03 13:35

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('accounts', '0002_journal'),
    ]

    operations = [
        migrations.AlterField(
            model_name='journal',
            name='username',
            field=models.CharField(max_length=30),
        ),
    ]
