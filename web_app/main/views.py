from django.http import HttpResponse
from django.shortcuts import render_to_response, redirect, get_object_or_404
from django.template import Context, RequestContext, loader
import json

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
