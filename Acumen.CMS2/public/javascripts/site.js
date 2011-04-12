
function isDefined (object) {
  if (typeof(object) == 'undefined') {
    return false;
  } else {
    return true;
  }
}

/**
 * @author Guy J. Murphy
 */
$(document).ready(function(){

    /* Flyout Menus */
    
    function selectOver(){
        $(this).find(".sub-menu").stop().fadeTo('fast', 1).show();
    }
    
    function selectOut(){
        $(this).find(".sub-menu").stop().fadeTo('fast', 0, function(){
            $(this).hide();
        });
    }
    
    var config = {
        sensitivity: 2, // number = sensitivity threshold (must be 1 or higher)    
        interval: 100, // number = milliseconds for onMouseOver polling interval    
        over: selectOver, // function = onMouseOver callback (REQUIRED)    
        timeout: 500, // number = milliseconds delay before onMouseOut    
        out: selectOut // function = onMouseOut callback (REQUIRED)    
    };
    
    $("div .sub-menu").css({
        'opacity': '0'
    });
    $("div .selector").hoverIntent(config);
    
    /* Subdued Inputs */
    
    subduedBoxes = $(".subdued");
    initialValues = {};
    
    for (var i = 0; i < subduedBoxes.length; i++) {
        var box = subduedBoxes[i];
        var id = $(box).attr("id");
        initialValues[id] = $(box).attr("value");
    }
    
    subduedBoxes.focus(function(){
        if ($(this).attr("value") == initialValues[id]) {
            $(this).attr("value", "");
        }
    });
	
    subduedBoxes.blur(function(){
        if ($(this).attr("value") == "") {
            $(this).attr("value", initialValues[id]);
        }
    });
	
	/* Edit In Place */
	
	// Values for the /metadata/view and the topic title
	$(".metadata-value-edit").editInPlace({
		url:			      "/metadata/inline/",
		element_id:		  "path",
		show_buttons:	  false, /*use ENTER and ESC keys*/
		value_required:	true
	});
	
	// Values for the /occurence/view
	$(".occurence-value-edit").editInPlace({
		url:			      "/occurence/inline/",
		element_id:		  "path",
		show_buttons:	  false, /*use ENTER and ESC keys*/
		value_required:	true
	});
	
	// Values for the /associations/view
	$(".association-value-edit").editInPlace({
		url:			      "/association/inline/",
		element_id:		  "path",
		show_buttons:	  false, /*use ENTER and ESC keys*/
		value_required:	true
	});
	
	// Testing for the 'Area' value on the left nav.
	$(".area-select").editInPlace({
		field_type:		  "select",
		url:			      "#",
		element_id:		  "topic-id",
		select_options:	"Acumen:acumen,London:acumen_london,Tenerife:acumen_tenerife",
		value_required:	true
	});


 	/* Text Editor */
 	try {
 	  $("#editor").markItUp(mySettings);
 	} catch (err) {}
  	
	/* Syntax Highlighter */
	try {
    SyntaxHighlighter.config.clipboardSwf     = '/javascripts/SyntaxHighlighter/scripts/clipboard.swf';
    SyntaxHighlighter.defaults['class-name']  = 'code-block';
    SyntaxHighlighter.all();
  } catch (err) {}
  
  /* Autocomplete */
  
  function findValue(li) {
	  if( li == null ){
			return alert("No match!");
	  }								
	  if( !!li.extra ){
			var sValue = li.extra[0];
		} else {
	  	var sValue = li.selectValue; 
	  }
	}
										
	function selectItem(li) {
	  findValue(li);
	}
  
  $(".autocomplete").autocomplete("/search/autocomplete",
  {
    delay:10,
		minChars:1,
		matchSubset:0,
		onItemSelect:selectItem,
		onFindValue:findValue,
		autoFill:true,
		maxItemsToShow:20
  });
  
  
  $('.pop-out-head').hover(
    function () {
      $(this).parent().addClass('hover');
    },
    function () {
      $(this).parent().removeClass('hover');
    }
  );
  
  $('.pop-out-head').click(
    function() {
      var body = $('.pop-out-body', $(this).parent());
      if ($(body).attr('open') == 'yes') {
         $(body).attr('open', 'no');
         $(body).toggle();
         $(body).css('z-index', 10);
      } else {
        $('.pop-out-body').each(function (index) {
          $(this).attr('open', 'no');
          $(this).hide();
          $(this).css('z-index', 10);
        });
        $(body).attr('open', 'yes');
        $(body).css('z-index', 20);
        $(body).toggle();
      }
    }
  );
  
  /* Galleria */
  if (isDefined(Galleria)) {
    Galleria.loadTheme('/javascripts/galleria/themes/classic/galleria.classic.js');
    $('#topic_images').galleria({
      autoplay: true
    });
  }

});
