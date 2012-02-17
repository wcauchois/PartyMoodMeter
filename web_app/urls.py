from django.conf.urls.defaults import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
  url(r'^places.json$', 'main.views.places'),
  url(r'^submit_sensor$', 'main.views.submit_sensor'),
    # Examples:
    # url(r'^$', 'web_app.views.home', name='home'),
    # url(r'^web_app/', include('web_app.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)
