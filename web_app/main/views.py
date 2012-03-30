from django.http import HttpResponse
from django.shortcuts import render_to_response, redirect, get_object_or_404
from django.template import Context, RequestContext, loader
from django.views.decorators.csrf import csrf_exempt

import json
import logging
from models import *

def json_result(view):
  def view_prime(*args):
    return HttpResponse(json.dumps(view(*args)), mimetype='application/json')
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
  print payload['hi']
  return HttpResponse('')

###############################################################################
###### Room Views
###############################################################################

def mood_value(request, room_id):
  pass

def mood(request, room_id):
  context = RequestContext(request, {
    'room_id': room_id
  })
  return render_to_response('mood.html', context)

