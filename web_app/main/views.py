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
 return ['hello', 'world', 'this', 'is', 'a', 'rather', 'long', 'list']
