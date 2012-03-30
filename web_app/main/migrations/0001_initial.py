# -*- coding: utf-8 -*-
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'MoodReading'
        db.create_table('main_moodreading', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('room_id', self.gf('django.db.models.fields.IntegerField')()),
            ('timestamp', self.gf('django.db.models.fields.IntegerField')(default=0)),
            ('user_id', self.gf('django.db.models.fields.CharField')(max_length=255)),
            ('mood', self.gf('django.db.models.fields.FloatField')(default=0)),
        ))
        db.send_create_signal('main', ['MoodReading'])

    def backwards(self, orm):
        # Deleting model 'MoodReading'
        db.delete_table('main_moodreading')

    models = {
        'main.moodreading': {
            'Meta': {'object_name': 'MoodReading'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'mood': ('django.db.models.fields.FloatField', [], {'default': '0'}),
            'room_id': ('django.db.models.fields.IntegerField', [], {}),
            'timestamp': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'user_id': ('django.db.models.fields.CharField', [], {'max_length': '255'})
        }
    }

    complete_apps = ['main']