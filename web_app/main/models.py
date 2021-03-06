from django.db import models

class MoodReading(models.Model):
  room_id = models.IntegerField()
  timestamp = models.IntegerField(default=0)
  user_id = models.CharField(blank=False, max_length=255)
  mood = models.FloatField(blank=False, default=0)

