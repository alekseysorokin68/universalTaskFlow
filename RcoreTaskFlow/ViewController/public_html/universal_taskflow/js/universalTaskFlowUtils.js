window.universalTaskFlow = 
{
  notImpl: function(event) 
  {
    if (event) 
    {
      event.cancel();
    }
    window.alert("Пока не реализовано");
  },
  test1 : function() 
  {
    alert('test1');
  },
  
  _columnPropChange : function (event) 
  {
    var propertyName = event.getPropertyName();
    if (propertyName == 'width')
    {
      AdfCustomEvent.queue(event.getSource(),
         "_myCustomEvent",
         {width: event.getNewValue()},
         true);
    }
  }

};

  function _reloadPageSpecial()
  {
    var loc = window.location.toString();
    var index = loc.indexOf('?');
    if (index > 0) 
    {
      loc = _removeParameter(loc,'_afrLoop');
      loc = _removeParameter(loc,'_afrWindowMode');
      loc = _removeParameter(loc,'_afrWindowId');
      loc = _removeParameter(loc,'_adf.ctrl-state');
    }
    
    //alert("@loc ="+loc);
    window.location.href = loc;
  }

  function _removeParameter (loc,parameter) 
  {
    var i = loc.indexOf('?'+parameter+"=");
    if (i >= 0) 
    {
      j = loc.indexOf('&',i);
      if (j >= 0) 
      {
        var loc1 = loc.substring(0, i+1);
        var loc2 = loc.substring(j+1);
        loc = loc1 + loc2;
      }
      else 
      {
        loc = loc.substring(0, i);
      }
    }
    else 
    {
      i = loc.indexOf('&'+parameter+"=");
      if (i >= 0) 
      {
        j = loc.indexOf('&',i+1);
        if (j >= 0) 
        {
          var l1 = loc.substring(0, i+1);
          var l2 = loc.substring(j+1);
          loc = l1 + l2;
        }
        else 
        {
          loc = loc.substring(0, i);
        }
      }
    }
    return loc;
  }




