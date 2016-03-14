// jQuery extensions
$(function()
{
    $.fn.originalAppend = $.fn.append;
    
    $.fn.append = function()
    {
	return $.fn.originalAppend.apply(this, arguments).trigger('appended');
    };
    
    $.fn.originalPrepend = $.fn.prepend;
    
    $.fn.prepend = function()
    {
	return $.fn.originalPrepend.apply(this, arguments).trigger('prepended');
    };
    
    $.fn.originalText = $.fn.text;
    
    $.fn.text = function()
    {
	if(arguments.length === 0)
	{
	    return $.fn.originalText.apply(this, arguments);
	}
	else
	{
	    $(this).removeData('formatted').removeAttr('data-formatted');
	    
	    return $.fn.originalText.apply(this, arguments).trigger('texted');
	}
    };
    
    $.fn.originalHtml = $.fn.html;
    
    $.fn.html = function()
    {
	if(arguments.length === 0)
	{
	    return $.fn.originalHtml.apply(this, arguments);
	}
	else
	{
	    $(this).removeData('formatted').removeAttr('data-formatted');
	    
	    return $.fn.originalHtml.apply(this, arguments).trigger('htmled');
	}
    };
    
    $.fn.originalInsertAfter = $.fn.insertAfter;
    
    $.fn.insertAfter = function()
    {
	return $.fn.originalInsertAfter.apply(this, arguments).trigger('insertedAfter');
    };
    
    $.fn.originalInsertBefore = $.fn.insertBefore;
    
    $.fn.insertBefore = function()
    {
	return $.fn.originalInsertBefore.apply(this, arguments).trigger('insertedBefore');
    };
    
    $.fn.originalFocus = $.fn.focus;
    
    $.fn.focus = function()
    {
	return isMobile() ? this : $.fn.originalFocus.apply(this, arguments).trigger('focused');
    };
});
// End jQuery extensions

// Form Handler
$(document).on('submit', 'form', function(e)
{
    e.stopPropagation();
    e.preventDefault();
    
    var $form = $(this);
    
    $form.find('[type="submit"]').attr('disabled', '');
    
    var action = {
	
	stop: false,
	onSuccess: null,
	onError: null,
	form: $form[0]
    };
    
    $form.trigger('ajaxsubmit', action);
    
    if(action.stop === false)
    {
	var isUpload = ($(action.form).attr('enctype') == 'multipart/form-data');
	var url = $(action.form).attr('action');
	var type = $(action.form).attr('method');
	
	$(action.form).find('input, textarea').each(function()
	{
	    $(this).val($.trim($(this).val()));
	});
	
	var data = isUpload ? new FormData(action.form) : $(action.form).serialize();
	
	var undefined;
	
	var onComplete = function(data, statusText, jqXHR, result)
	{
	    var contentType = jqXHR.getResponseHeader('Content-Type') || '';
	    
	    contentType = contentType.split(';')[0];
	    
	    var proceed = true;
	    
	    if(contentType.toUpperCase() == 'TEXT/HTML')
	    {
		if(result.success === true)
		{
		    if(typeof action.onSuccess === 'function')
		    {
			proceed = action.onSuccess(result, jqXHR);
		    }
		}
		else
		{
		    if(typeof action.onError === 'function')
		    {
			proceed = action.onError(result, jqXHR);
		    }
		}
		
		if(proceed !== false && jqXHR.status !== 403)
		{
		    load(jqXHR.getResponseHeader('X-Url'));
		}
	    }
	    else
	    {
		if(result.success === true)
		{
		    result.message = result.message || 'Feito!';
		    
		    if(typeof action.onSuccess === 'function')
		    {
			proceed = action.onSuccess(result, jqXHR);
		    }
		    
		    if(proceed !== false)
		    {
			toastr.success(result.message);
		    }
		}
		else
		{
		    result.message = typeof result.message === 'string' ? Utils.replace('responses.' + result.message.replace(/\"/g, '')) : result.message;
		    result.message = result.message || 'Erro ' + jqXHR.status;
		    
		    if(typeof action.onError === 'function')
		    {
			proceed = action.onError(result, jqXHR);
		    }
		    
		    if(proceed !== false)
		    {
			toastr.error(result.message);
		    }
		}
	    }
	    
	    $form.find('[type="submit"]').removeAttr('disabled');
	};
	
	var onSuccess = function(data, statusText, jqXHR)
	{
	    onComplete(data, statusText, jqXHR, {
		success: true,
		data: data
	    });
	};
	
	var onError = function(jqXHR, statusText, errorThrown)
	{
	    onComplete(jqXHR.responseJSON, jqXHR.statusText, jqXHR, {
		success: false,
		data: jqXHR.responseJSON,
		message: jqXHR.responseJSON
	    });
	};
	
	if(url !== undefined && type !== undefined)
	{
	    var options = {
		
		url: url,
		data: data,
		type: type.toUpperCase(),
		success: onSuccess,
		error: onError
	    };
	    
	    if(isUpload)
	    {
		options['mimeType'] = 'multipart/form-data';
		options['contentType'] = false;
		options['cache'] = false;
		options['processData'] = false;
	    }
	    
	    $.ajax(options);
	}
	else
	{
	    onError(null, 'form method or action was not defined', null);
	}
    }
    else
    {
	$form.find('[type="submit"]').removeAttr('disabled');
    }
    
    return false;
});
// End Form Handler

// Menu
$(function()
{
    var $dashboard = $('[href$="/dashboard"]');
    $dashboard.attr('href', $dashboard.attr('href') + '?selectedFormattedDate=' + encodeURIComponent(Utils.format.date(new Date())));
});
// End Menu

// Modal Autenticacao - Ajax Error
$(function()
{
    var ignoredUrls = ['/autenticacao/login'];
    var forbiddenAjaxRequests = [];
    
    var isIgnoredUrl = function(url)
    {
	for(var i = 0, len = ignoredUrls.length; i < len; i++)
	{
	    if(url.indexOf(ROOT) !== 0)
	    {
		if(url.indexOf(ROOT + ignoredUrls[i]) !== 0)
		{
		    return true;
		}
	    }
	    else
	    {
		if(url.indexOf(ignoredUrls[i]) !== 0)
		{
		    return true;
		}
	    }
	}
	
	return false;
    };
    
    $(document).on('ajaxsubmit', '#modalAutenticacao form', function(e, action)
    {
	var $this = $(this);
	
	if($this.find('[name="matriz"]').val().length === 0)
	{
	    toastr.error('Matriz deve ser preenchida');
	    
	    action.stop = true;
	}
	
	var $newForm = $(action.form).clone();
	
	if($newForm.find('[name="estabelecimentoPessoaId"]').val().length === 0)
	{
	    $newForm.find('[name="estabelecimentoPessoaId"]').remove();
	}
	
	action.form = $newForm[0];
	
	action.onSuccess = function(data)
	{
	    $('#modalAutenticacao').modal('hide');
	    
	    for(var i = 0, len = forbiddenAjaxRequests.length; i < len; i++)
	    {
		$.ajax(forbiddenAjaxRequests[i]);
	    }
	    
	    forbiddenAjaxRequests = [];
	    
	    return false;
	};
    });
    
    $.ajaxSetup({
	headers: {
	    'X-Time-Zone': moment(new Date()).format('ZZ')
	},
	statusCode: {
	    403: function(a, b, c)
	    {
		if(!$('#modalAutenticacao').hasClass('in'))
		{
		    forbiddenAjaxRequests.push(this);
		    
		    $('#modalAutenticacao #iptUsuarioSenha').val('');
		    $('#modalAutenticacao').modal('show');
		}
	    }
	}
    });
    
    $('#modalAutenticacao').on('shown.bs.modal', function()
    {
	$('#iptUsuarioSenha', this).focus();
    });
});
// End Modal Autenticacao - Ajax Error

// Modal External Link
$(function()
{
    $('#modalExternalLink').on('shown.bs.modal', function()
    {
	$('button:first', this).focus();
    });
    
    $(document).on('click', '#modalExternalLink .continue', function()
    {
	$('#modalExternalLink').modal('hide');
    });
});
// End Modal External Link

// Upload, Datepicker, Popover, Tooltip, Mask, MaskMoney, Replace, Format,
// Move, Display-Chooser, Autofocus small devices, Links to window.location
$(function()
{
    var applyOnDocument = function()
    {
	$('[data-upload]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('uploaded') === undefined)
	    {
		$this.attr('data-uploaded', '');
		
		var options = {
		    limit: $this.attr('data-limit') || null,
		    maxSize: 10 * 1024 * 1024,
		    extensions: null,
		    changeInput: $('#filerChangeInputTemplate').html(),
		    showThumbs: true,
		    appendTo: null,
		    theme: 'dragdropbox',
		    templates: {
			box: $('#filerBoxTemplate').html(),
			item: $('#filerItemTemplate').html(),
			itemAppend: $('#filerItemAppendTemplate').html(),
			progressBar: $('#filerProgressBarTemplate').html(),
			itemAppendToEnd: false,
			removeConfirmation: false,
			_selectors: {
			    list: '.jFiler-item-list',
			    item: '.jFiler-item',
			    progressBar: '.bar',
			    remove: '.jFiler-item-trash-action',
			}
		    },
		    uploadFile: {
			url: $this.data('action'),
			data: {},
			type: $this.data('method') !== undefined || $this.data('method') !== '' ? $this.data('method').toUpperCase() : 'POST',
			enctype: 'multipart/form-data',
			beforeSend: function()
			{
			    try
			    {
				if(typeof window[$this.data('before-callback')] === 'function')
				{
				    window[$this.data('before-callback')].call(this);
				}
			    }
			    catch(exception)
			    {
				
			    }
			},
			success: function(data, el, l, p, o, s, id, statusText, jqXHR)
			{
			    toastr.success('Feito!');
			    
			    var parent = el.find('.jFiler-jProgressBar').parent();
			    
			    el.find('.jFiler-jProgressBar').fadeOut('slow', function()
			    {
				el.find('.jFiler-item-error').hide();
				$($('#filerSuccessTemplate').html()).hide().appendTo(parent).fadeIn('slow');
			    });
			    
			    try
			    {
				if(typeof window[$this.data('success-callback')] === 'function')
				{
				    window[$this.data('success-callback')].call(this, data);
				}
			    }
			    catch(exception)
			    {
				
			    }
			},
			error: function(el, l, p, o, s, id, jqXHR, statusText, errorThrown)
			{
			    if(jqXHR.status !== 403)
			    {
				var message = jqXHR.responseJSON;
				message = typeof message === 'string' ? Utils.replace('responses.' + message.replace(/\"/g, '')) : message;
				message = message || 'Erro ' + jqXHR.status;
				
				toastr.error(message);
				
				var parent = el.find('.jFiler-jProgressBar').parent();
				
				el.find('.jFiler-jProgressBar').fadeOut('slow', function()
				{
				    el.find('.jFiler-item-success').hide();
				    $($('#filerErrorTemplate').html()).hide().appendTo(parent).fadeIn('slow');
				});
				
				try
				{
				    if(typeof window[$this.data('error-callback')] === 'function')
				    {
					window[$this.data('error-callback')].call(this);
				    }
				}
				catch(exception)
				{
				    
				}
			    }
			},
			statusCode: {},
			onProgress: function()
			{
			},
		    },
		    dragDrop: {
			dragEnter: function()
			{
			},
			dragLeave: function()
			{
			},
			drop: function()
			{
			},
		    },
		    addMore: true,
		    clipBoardPaste: true,
		    excludeName: null,
		    beforeShow: function()
		    {
			return true
		    },
		    onSelect: function()
		    {
		    },
		    afterShow: function()
		    {
		    },
		    onRemove: function()
		    {
		    },
		    onEmpty: function()
		    {
		    },
		    captions: {
			button: 'Escolha arquivos',
			feedback: 'Escolha arquivos para enviar',
			feedback2: 'Arquivos escolhidos',
			drop: 'Arraste aqui seus arquivos',
			removeConfirmation: 'Você tem certeza que deseja remover esse arquivo?',
			errors: {
			    filesLimit: 'É possível enviar somente {{fi-limit}} arquivos.',
			    filesType: 'É possível enviar somente imagens',
			    filesSize: '{{fi-name}} muito grande! Tamanho máximo: {{fi-maxSize}} MB.',
			    filesSizeAll: 'Os arquivos escolhidos são muito grandes! Por favor, diminua para {{fi-maxSize}} MB.'
			}
		    }
		};
		
		$this.data('filerOptions', options).filer(options);
	    }
	});
	
	$('[data-datepicker]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('datepickered') === undefined)
	    {
		$this.datepicker();
		
		$this.attr('data-datepickered', '');
	    }
	});
	
	$('[data-popover]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('popovered') === undefined)
	    {
		$this.popover();
		
		$this.attr('data-popovered', '');
	    }
	});
	
	$('[data-tooltip]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('tooltiped') === undefined)
	    {
		$this.tooltip();
		
		$this.attr('data-tooltiped', '');
	    }
	});
	
	$('[data-mask]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('masked') === undefined)
	    {
		var mask = $this.data('mask');
		
		$this.mask(mask);
		
		$this.attr('data-masked', '');
	    }
	});
	
	$('[data-replace]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('replaced') === undefined)
	    {
		var replace = $this.data('replace');
		var hasValueProperty = ($this.data('replace-text') === undefined && (this['value'] !== undefined));
		var replaced = Utils.replace(replace);
		
		hasValueProperty ? $this.val(replaced) : $this.originalText(replaced);
		
		$this.attr('data-replaced', '');
	    }
	});
	
	$('[data-mask-money]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('masked-money') === undefined)
	    {
		$this.maskMoney();
		
		if($this.val().length > 0)
		{
		    try
		    {
			var value = $this.data('precision') == 0 ? parseInt($this.val()) : parseFloat($this.val());
			
			if(!isNaN(value))
			{
			    $this.maskMoney('mask', value);
			}
		    }
		    catch(exception)
		    {
			
		    }
		}
		
		$this.attr('data-masked-money', '');
		
		if($this.data('allow-null') == true && $this.data('hide-clear') != true)
		{
		    var wrap = true;
		    
		    if($this.parent().length > 0)
		    {
			if($this.parent().css('position') == 'relative' && $this.parent().css('float') == 'none')
			{
			    $this.parent().addClass('mask-money-remove');
			    
			    wrap = false;
			}
		    }
		    
		    if(wrap)
		    {
			$this.wrap('<div style="position: relative;" class="mask-money-remove"></div>');
		    }
		    
		    $this.parent().append('<div style="position: absolute; right: 5px; top: 50%; margin-top: -8px; z-index: 3;"><a href="javascript: void(0);"><i class="fa fa-remove"></i></a></div>');
		    $this.css('padding-right', '20px');
		}
	    }
	});
	
	$('[data-format]').each(function(i)
	{
	    var $this = $(this);
	    
	    if($this.data('formatted') === undefined)
	    {
		$this.attr('data-formatted', '');
		
		var format = $this.data('format');
		
		if(Utils.format[format] !== undefined)
		{
		    var hasValueProperty = (this['value'] !== undefined);
		    var formatFunction = Utils.format[format];
		    var $data = $this.data();
		    var value = $data.formatValue !== undefined ? $data.formatValue : hasValueProperty ? $this[0].value : $this.originalText();
		    
		    delete $data['format'];
		    
		    hasValueProperty ? $this.val(formatFunction(value, $data)) : $this.originalText(formatFunction(value, $data));
		}
	    }
	});
	
	$('[data-move]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('moved') === undefined)
	    {
		// before we re-append it :-)
		$this.attr('data-moved', '');
		
		var selector = $this.data('move');
		
		var before = ($this.data('move-before') !== undefined);
		var insert = ($this.data('move-insert') !== undefined);
		
		if(insert !== true)
		{
		    if(before === true)
		    {
			$this.prependTo(selector);
		    }
		    else
		    {
			$this.appendTo(selector);
		    }
		}
		else
		{
		    if(before === true)
		    {
			$this.insertBefore(selector);
		    }
		    else
		    {
			$this.insertAfter(selector);
		    }
		}
		
		var $thisIsAutofocus = $this.is('[autofocus]');
		var $thisHasAutofocus = $this.find('[autofocus]').length > 0;
		
		if($thisIsAutofocus || $thisHasAutofocus && !isMobile())
		{
		    if($thisIsAutofocus)
		    {
			$this.focus();
		    }
		    else if($thisHasAutofocus)
		    {
			$this.find('[autofocus]').focus();
		    }
		}
	    }
	});
	
	$('[data-display-chooser]').each(function()
	{
	    var $this = $(this);
	    
	    if($this.data('displayed-choosed') === undefined)
	    {
		$this.attr('data-displayed-choosed', '');
		
		$this.change(function()
		{
		    var $content = $($this.attr('data-content'));
		    var value = $this.val();
		    var $items = $content.find(value);
		    
		    if($items.length === 0)
		    {
			$content.find('> *').addClass('hide');
		    }
		    else
		    {
			$items.removeClass('hide').siblings().addClass('hide');
		    }
		});
	    }
	});
	
	if(isMobile())
	{
	    $('[autofocus]').each(function()
	    {
		$(this).removeAttr('autofocus').blur();
	    });
	}
	
	$('a').each(function()
	{
	    var $this = $(this);
	    
	    if($this.parents('#modalExternalLink').length === 0 && $this.attr('onClick') === undefined && !Utils.startsWith(($this.attr('href') || '').toLowerCase(), 'javascript:') && ($this.attr('href') || '').length > 0 && !Utils.startsWith(($this.attr('href') || ''), '#'))
	    {
		$this.off('click.mobilefix');
		$this.on('click.mobilefix', function(e)
		{
		    load($this.attr('href'), 0, e, (($this.attr('target') || '').toLowerCase() === '_blank' ? true : undefined));
		    
		    return false;
		});
	    }
	});
    };
    
    applyOnDocument();
    
    $(document).on('click', '.mask-money-remove a', function()
    {
	var $maskMoney = $(this).parents('.mask-money-remove').find('[data-mask-money]');
	
	$maskMoney.val('').trigger('mask-money-removed');
    });
    
    $(document).off('appended').off('prepended').off('htmled').off('insertedAfter').off('insertedBefore');
    
    $(document).on({
	appended: applyOnDocument,
	prepended: applyOnDocument,
	texted: applyOnDocument,
	htmled: applyOnDocument,
	insertedAfter: applyOnDocument,
	insertedBefore: applyOnDocument
    });
});
// End Upload Datepicker, Popover, Tooltip, Mask, MaskMoney, Replace, Format,
// Move, Display-Chooser, Autofocus small devices, Links to window.location

// Begin toastr
$(function()
{
    toastr.options = {
	closeButton: false,
	newestOnTop: true,
	progressBar: true,
	preventDuplicates: true,
	showDuration: 300,
	hideDuration: 1000,
	timeOut: 4000,
	extendedTimeOut: 2000,
	showEasing: 'swing',
	hideEasing: 'linear',
	showMethod: 'fadeIn',
	hideMethod: 'fadeOut'
    };
});
// End toastr

// Begin Modal
$(function()
{
    $(document).on('show.bs.modal', '.modal', function()
    {
	$('body').addClass('modal-open-fix');
    });
    
    $(document).on('hide.bs.modal', '.modal', function()
    {
	var $modal = $('.modal');
	
	if(($modal.filter('.in').length > 1 && !$modal.hasClass('in')) || ($modal.filter('.in').length === 1 && $modal.hasClass('in')))
	{
	    $('body').removeClass('modal-open-fix');
	}
    });
});
// End Modal

// Global
var ctrlKeyIsPressed = false;
var cmdOrWindowKeyIsPressed = false;

$(function()
{
    $(document).keydown(function(e)
    {
	ctrlKeyIsPressed = e.which === 17;
	cmdOrWindowKeyIsPressed = e.which === 91 || e.which === 92;
    });
    
    $(document).keyup(function()
    {
	ctrlKeyIsPressed = false;
	cmdOrWindowKeyIsPressed = false;
    });
    
    $(window).on('blur focus', function(e)
    {
	var prevType = $(this).data('prevType');
	
	if(prevType != e.type)
	{
	    switch(e.type)
	    {
		case 'blur':
		{
		    ctrlKeyIsPressed = false;
		    cmdOrWindowKeyIsPressed = false;
		    
		    break;
		}
		    
		case 'focus':
		{
		    break;
		}
	    }
	}
	
	$(this).data("prevType", e.type);
    })
});

function hasPerfil(perfil)
{
    return $.inArray(perfil, USUARIO_PERFIS) > -1;
}

function reload(milliseconds)
{
    milliseconds = milliseconds || 0;
    
    setTimeout(function()
    {
	window.location.reload();
    }, milliseconds);
}

function load(location, milliseconds, e, blank)
{
    milliseconds = milliseconds || 0;
    
    setTimeout(function()
    {
	blank = blank || (e !== undefined ? e.which === 2 || ctrlKeyIsPressed || cmdOrWindowKeyIsPressed : false);
	
	if(blank)
	{
	    openExternalLink(location);
	}
	else
	{
	    window.location = location;
	}
    }, milliseconds);
}

function isMobile()
{
    return (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0, 4)));
}

function openExternalLink(link)
{
    var $modalExternalLink = $('#modalExternalLink');
    
    $modalExternalLink.find('.link').text(link);
    $modalExternalLink.find('.continue').attr('href', link);
    
    $modalExternalLink.modal('show');
}

function sessionRenew()
{
    $.ajax({
	url: ROOT + '/autenticacao/sessionRenew',
	complete: function()
	{
	    setTimeout(function()
	    {
		sessionRenew();
	    }, 5000 * 60);
	}
    })
}

function pdf(selectorOrElement, options)
{
    var $content = $(selectorOrElement).clone();
    var styleSheets = document.styleSheets;
    var $copyright = $('<div class="text-right"><small><i>' + APP_NAME + ' - ' + APP_VERSION + '</i> (gerado em ' + Utils.format.date(new Date(), 'DD/MM/YYYY [às] HH:mm') + ')</small></div>');
    var $fullContent = $('<div class="pdf-root"><div class="pdf-html"><div class="pdf-body"></div></div></div>').find('.pdf-body').append($copyright).append($content).parents('.pdf-root');
    var selectorsMap = {};
    
    for(var i = 0, iLen = styleSheets.length; i < iLen; i++)
    {
	for(var j = 0, jLen = styleSheets[i].rules.length; j < jLen; j++)
	{
	    try
	    {
		var selectorText = styleSheets[i].rules[j].selectorText;
		var trimmedSelectorText = $.trim(selectorText);
		
		if(trimmedSelectorText === 'html')
		{
		    selectorText = '.pdf-html';
		}
		else if(trimmedSelectorText === 'body')
		{
		    selectorText = '.pdf-body';
		}
		else
		{
		    if(selectorText.indexOf(',') > -1)
		    {
			var selectorsText = selectorText.split(',');
			
			for(var k = 0, kLen = selectorsText.length; k < kLen; k++)
			{
			    var trimmedCurrentSelectorText = $.trim(selectorsText[k]);
			    
			    if(trimmedCurrentSelectorText === 'html')
			    {
				trimmedCurrentSelectorText = '.pdf-html';
			    }
			    else if(trimmedCurrentSelectorText === 'body')
			    {
				trimmedCurrentSelectorText = '.pdf-body';
			    }
			    
			    selectorsText[k] = trimmedCurrentSelectorText;
			}
			
			selectorText = selectorsText.join(',');
		    }
		}
		
		selectorText = selectorText.replace(/\:\:before/gi, ' .pdf-before').replace(/\:\:after/gi, ' .pdf-after');
		
		if(selectorText.indexOf('.pdf-before') > -1)
		{
		    var selectors = selectorText.split(',');
		    
		    for(var k = 0, kLen = selectors.length; k < kLen; k++)
		    {
			var trimmedSelectorText = $.trim(selectors[k]).toLowerCase();
			
			if(trimmedSelectorText.indexOf('.pdf-before') > -1 && $fullContent.find(trimmedSelectorText).length === 0)
			{
			    $('<span class="pdf-before" style="line-height: 0; font-size: 1em;">&nbsp;</span>').insertBefore($fullContent.find(trimmedSelectorText.replace(/\ \.pdf\-before/, '')));
			}
		    }
		}
		
		if(selectorText.indexOf('.pdf-after') > -1)
		{
		    var selectors = selectorText.split(',');
		    
		    for(var k = 0, kLen = selectors.length; k < kLen; k++)
		    {
			var trimmedSelectorText = $.trim(selectors[k]).toLowerCase();
			
			if(trimmedSelectorText.indexOf('.pdf-after') > -1 && $fullContent.find(trimmedSelectorText).length === 0)
			{
			    $('<span class="pdf-after" style="line-height: 0; font-size: 1em;">&nbsp;</span>').insertAfter($fullContent.find(trimmedSelectorText.replace(/\ \.pdf\-after/, '')));
			}
		    }
		}
		
		$fullContent.find(selectorText).each(function(contentSelectorTextIndex)
		{
		    var cssText = styleSheets[i].rules[j].style.cssText.replace(/content(\ )*\:([^\;]+)\;/gi, '');
		    this.style.cssText += cssText;
		    
		    var selectorsText = selectorText.split(',');
		    
		    for(var k = 0, kLen = selectorsText.length; k < kLen; k++)
		    {
			var trimmedSelectorText = $.trim(selectorsText[k]).toLowerCase();
			
			if(selectorsMap[trimmedSelectorText] === undefined)
			{
			    selectorsMap[trimmedSelectorText] = {
				cssText: cssText,
				elements: [this]
			    };
			}
			else
			{
			    selectorsMap[trimmedSelectorText].cssText += cssText;
			    selectorsMap[trimmedSelectorText].elements.push(this);
			}
		    }
		});
	    }
	    catch(exception)
	    {
		
	    }
	}
    }
    
    if(typeof options === 'object')
    {
	if(typeof options.before === 'function')
	{
	    options.before($content, $fullContent);
	}
    }
    
    $fullContent.find('.pdf-body').css('height', 'auto');
    
    var html = $fullContent.html().replace('<div class="pdf-html"', '<html').replace('<div class="pdf-body"', '<body').replace(/<\/div><\/div>$/, '</body></html>');
    
    $.ajax({
	url: ROOT + '/pdf',
	type: 'POST',
	data: 'url=true&content=' + encodeURIComponent(html),
	success: function(data)
	{
	    openExternalLink(data);
	}
    });
}

function googleViewer(url)
{
    return 'https://docs.google.com/viewer?url=' + url;
}
// End Global

// Utils
var Utils = {
    textSelection: {
	disable: function($el)
	{
	    $el.css({
		'MozUserSelect': 'none',
		'webkitUserSelect': 'none'
	    }).attr('unselectable', 'on').bind('selectstart.utils', function()
	    {
		return false;
	    });
	},
	enable: function($el)
	{
	    $el.css({
		'MozUserSelect': '',
		'webkitUserSelect': ''
	    }).attr('unselectable', 'off').unbind('selectstart.utils');
	}
    },
    fillLeftZero: function(numberDesiredLength, number)
    {
	number = '' + number;
	
	if(number.length < numberDesiredLength)
	{
	    for(var i = number.length; i < numberDesiredLength; i++)
	    {
		number = '0' + number;
	    }
	}
	
	return number;
    },
    startsWith: function(string, prefix)
    {
	return string.indexOf(prefix) === 0;
    },
    endsWith: function(string, suffix)
    {
	return string.indexOf(suffix, string.length - suffix.length) !== -1;
    },
    parseSemicolonObject: function(string)
    {
	string = string || '';
	
	if(string.length === 0)
	{
	    return null;
	}
	
	var object = {};
	
	var fragments = string.split(';');
	
	for(var i = 0, len = fragments.length; i < len; i++)
	{
	    var equalsPosition = fragments[i].indexOf('=');
	    var objectName = fragments[i].substring(equalsPosition, 0);
	    var objectValue = fragments[i].substring(equalsPosition + 1, fragments[i].length);
	    
	    object[objectName] = objectValue;
	}
	
	return object;
    },
    searchByCep: function(cep, successCallback)
    {
	$.ajax({
	    url: ROOT + '/endereco/cep/' + cep.replace(/\-/g, ''),
	    success: function(data)
	    {
		if(typeof successCallback === 'function')
		{
		    successCallback(data);
		}
	    }
	});
    },
    format: {
	html: function(html)
	{
	    if(html !== null && typeof html === 'string')
	    {
		html = html.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#039;').replace(/=/g, '&#61;');
	    }
	    
	    return html;
	},
	telefone: function(telefone)
	{
	    if(telefone !== null)
	    {
		telefone = telefone.replace(/\D/g, '');
		telefone = telefone.replace(/^(\d{2})(\d)/g, '+$1 $2');
		telefone = telefone.replace(/ (\d{2})(\d)/g, ' ($1) $2');
	    }
	    
	    return telefone;
	},
	cpf: function(cpf)
	{
	    if(cpf !== null)
	    {
		cpf = cpf.replace(/\D/g, '');
		cpf = cpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/g, '$1.$2.$3-$4');
	    }
	    
	    return cpf;
	},
	cnpj: function(cnpj)
	{
	    if(cnpj !== null)
	    {
		cnpj = cnpj.replace(/\D/g, '');
		cnpj = cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/g, '$1.$2.$3/$4-$5');
		
		return cnpj;
	    }
	    
	    return '';
	},
	cep: function(cep)
	{
	    if(cep !== null)
	    {
		cep = cep.replace(/\D/g, '');
		cep = cep.replace(/^(\d{5})(\d{3})$/g, '$1-$2');
		
		return cep;
	    }
	    
	    return '';
	},
	cnae: function(cnae)
	{
	    if(cnae !== null)
	    {
		cnae = cnae.replace(/\D/g, '');
		cnae = cnae.replace(/^(\d{2})(\d{2})(\d{1})(\d{2})$/g, '$1.$2.$3/$4');
		
		return cnae;
	    }
	    
	    return '';
	},
	money: function(value, dataSettings)
	{
	    if(value === null)
	    {
		return '';
	    }
	    else if(('' + value).length === 0)
	    {
		return '';
	    }
	    
	    try
	    {
		if(value instanceof BigDecimal)
		{
		    value = value.toString();
		}
		
		var $input = $('<input type="text" value="' + value + '" />');
		
		$input.maskMoney(dataSettings);
		
		if($input.data('precision') == 0)
		{
		    $input.maskMoney('mask', value);
		}
		else
		{
		    $input.maskMoney('mask', parseFloat(value));
		}
		
		return $input.val();
	    }
	    catch(exception)
	    {
		return '';
	    }
	},
	date: function(value, mask)
	{
	    if(value !== null)
	    {
		var defaultMask = 'YYYY-MM-DDTHH:mm:ss.SSS';
		
		mask = mask || defaultMask;
		
		if(typeof mask === 'object')
		{
		    mask = mask.dateMask;
		}
		
		return typeof value === 'string' ? value.length > 0 ? moment(value === 'now' ? new Date() : value).format(mask) : '' : moment(value).format(mask);
	    }
	    
	    return '';
	},
	endereco: function(endereco)
	{
	    if(endereco !== null)
	    {
		return endereco.logradouro + (endereco.complemento !== null ? ' - ' + endereco.complemento : '') + ', ' + endereco.numero + ' - ' + endereco.bairro + ' (' + endereco.cidade.nome + ' / ' + endereco.cidade.estado.estadoId + ')';
	    }
	    
	    return '';
	},
	minutes: function(minutes)
	{
	    minutes = parseInt(minutes);
	    
	    var hours = Math.floor(minutes / 60);
	    var leftMinutes = minutes % 60;
	    
	    return (hours < 10 ? '0' + hours : hours) + ':' + (leftMinutes < 10 ? '0' + leftMinutes : leftMinutes);
	},
	minutesAndSeconds: function(minutes, seconds)
	{
	    if(typeof seconds === 'object')
	    {
		minutes = seconds['minutes'];
		seconds = seconds['seconds'];
	    }
	    
	    return Utils.format.minutes(minutes) + ':' + (seconds < 10 ? '0' + seconds : seconds);
	}
    },
    
    toFixed: function(number, precision)
    {
	if(number !== null)
	{
	    number = '' + parseFloat(number);
	    
	    var parts = number.split('.');
	    var integerPart = parts[0];
	    var decimalPart = parts.length > 1 ? parts[1] : 0;
	    
	    if(decimalPart.length < precision)
	    {
		for(var i = 0, len = precision - decimalPart.length; i < len; i++)
		{
		    decimalPart += '0';
		}
	    }
	    else if(decimalPart.length > precision)
	    {
		decimalPart = decimalPart.substring(0, precision);
	    }
	    
	    return parseFloat(integerPart + '.' + (precision > 0 ? decimalPart : ''));
	}
	else
	{
	    return 0;
	}
    },
    
    __REPLACE__: {},
    
    replace: function(value, inverse)
    {
	switch(typeof value)
	{
	    case 'string':
	    {
		try
		{
		    var hasOptions = (typeof inverse === 'object');
		    
		    if(inverse)
		    {
			var path = value.split('.');
			var value = path.pop();
			
			path = path.length === 0 ? '' : path.join('.');
			
			var foundObject = eval('Utils.__REPLACE__.' + path);
			
			// look up for the value (index)
			for( var index in foundObject)
			{
			    if(hasOptions)
			    {
				if(inverse.insensitive === true)
				{
				    if(foundObject[index].toUpperCase() === value.toUpperCase())
				    {
					return index;
				    }
				}
			    }
			    else
			    {
				if(foundObject[index] === value)
				{
				    return index;
				}
			    }
			}
			
			return value;
		    }
		    
		    return eval('Utils.__REPLACE__.' + value);
		    
		}
		catch(exception)
		{
		    return value;
		}
	    }
		
	    case 'object':
	    {
		for(index in value)
		{
		    Utils.__REPLACE__[index] = value[index];
		}
	    }
	}
    },
    
    date: {
	getYear: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return moment(new Date(time)).format('YYYY');
	},
	
	getMonth: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return moment(new Date(time)).format('MM');
	},
	
	getWeek: function(time)
	{
	    time = time || new Date().getTime();
	    
	    var date = new Date(time);
	    var firstDay = new Date(date.getFullYear(), date.getMonth(), 1).getDay();
	    
	    return Math.ceil((date.getDate() + firstDay) / 7);
	},
	
	getDay: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return moment(new Date(time)).format('DD');
	},
	
	getNowTime: function(time)
	{
	    time = time || new Date().getTime();
	    
	    var date = moment(new Date(time)).format('YYYY-MM-DD');
	    var time = moment(new Date()).format('HH:mm:ss.SSS');
	    
	    return moment(date + ' ' + time, 'YYYY-MM-DD HH:mm:ss.SSS').toDate();
	},
	
	getMiddleTime: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return moment(moment(new Date(time)).format('YYYY-MM-DD') + ' 11:59:59.999', 'YYYY-MM-DD HH:mm:ss.SSS').toDate();
	},
	
	getFirstTime: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return moment(moment(new Date(time)).format('YYYY-MM-DD') + ' 00:00:00.000', 'YYYY-MM-DD HH:mm:ss.SSS').toDate();
	},
	
	getLastTime: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return moment(moment(new Date(time)).format('YYYY-MM-DD') + ' 23:59:59.999', 'YYYY-MM-DD HH:mm:ss.SSS').toDate();
	},
	
	getFirstDayOfWeek: function(time)
	{
	    time = time || new Date().getTime();
	    
	    var date = new Date(time);
	    var difference = date.getDate() - date.getDay();
	    
	    date.setDate(difference);
	    
	    return this.getFirstTime(date);
	},
	
	getLastDayOfWeek: function(time)
	{
	    time = time || new Date().getTime();
	    
	    var date = new Date(time);
	    var difference = date.getDate() - date.getDay() + 6;
	    
	    date.setDate(difference);
	    
	    return this.getLastTime(date);
	},
	
	getFirstDayOfMonth: function(time)
	{
	    time = time || new Date().getTime();
	    
	    var date = new Date();
	    
	    return this.getFirstTime(new Date(date.getFullYear(), date.getMonth(), 1));
	},
	
	getLastDayOfMonth: function(time)
	{
	    time = time || new Date().getTime();
	    
	    var date = new Date();
	    
	    return this.getLastTime(new Date(date.getFullYear(), date.getMonth() + 1, 0));
	},
	
	getFirstDayOfYear: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return this.getFirstTime(new Date(new Date(time).getFullYear(), 0, 1));
	},
	
	getLastDayOfYear: function(time)
	{
	    time = time || new Date().getTime();
	    
	    return this.getLastTime(new Date(new Date(time).getFullYear(), 11, 31));
	}
    }
};
// End Utils

// ios debug
// javascript:window.onerror=function(m,u,l){alert(m+"\n"+u+":"+l);};

