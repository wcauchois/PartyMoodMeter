from django.http import HttpResponse
from django.shortcuts import render_to_response, redirect, get_object_or_404
from django.template import Context, RequestContext, loader
from django.views.decorators.csrf import csrf_exempt

import json
import logging
from time import time
from models import *

def json_result(view):
  def view_prime(*args, **kwargs):
    return HttpResponse(json.dumps(view(*args, **kwargs)), mimetype='application/json')
  return view_prime

@json_result
def places(request):
 return [
   dict(name='Tim\'s Party', id=1, num_people=5),
   dict(name='Bill\'s Party', id=2, num_people=100),
   dict(name='DJ Tiesto at Safeco Field', id=3, num_people=50000),
 ]

def index(request):
  return HttpResponse('')

@csrf_exempt
def submit_sensor(request):
  logging.debug(request.raw_post_data)
  return HttpResponse('')

@csrf_exempt
def submit_mood(request):
  payload = json.loads(request.raw_post_data)

  user_id = payload['user_id']
  room_id = payload['room_id']
  mood = payload['mood']

  readings = MoodReading.objects.filter(user_id=user_id)
  if len(readings) == 0:
    reading = MoodReading()
    reading.user_id = user_id
  else:
    reading = readings[0]
  reading.room_id = room_id
  reading.mood = mood
  reading.timestamp = int(time())
  reading.save()

  return HttpResponse('')

###############################################################################
###### Room Views
###############################################################################

STALE_TIMEOUT = 10 # seconds

@json_result
def mood_value(request, room_id):
  readings = MoodReading.objects.filter(room_id=int(room_id))
  timely_readings = [r for r in readings if time() - r.timestamp < STALE_TIMEOUT]
  total = sum(r.mood for r in timely_readings)
  if len(timely_readings) == 0:
    average = 0.0
  else:
    average = total / float(len(timely_readings))
  return {'average': average}

def mood(request, room_id):
  context = RequestContext(request, {
    'room_id': room_id
  })
  return render_to_response('mood.html', context)

