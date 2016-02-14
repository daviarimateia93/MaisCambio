$(function()
{
    $('#usuario-form').on('ajaxsubmit', function(e, action)
    {
	// we want to hide notification, in case of user click on the button,
	// and lose input focus - triggering blur event
	if(USUARIO_NEW === true)
	{
	    if(!testSenha(true, true, ($('.toast-error').length > 0)))
	    {
		action.stop = true;
	    }
	}
	
	var $newForm = $(action.form).clone();
	
	$newForm.find('input[type="checkbox"]').each(function(i, e)
	{
	    $(this).prop('checked', $(action.form).find('input[type="checkbox"]:eq(' + i + ')').is(':checked'));
	});
	
	$newForm.find('select').each(function(i, e)
	{
	    var $this = $(this);
	    
	    $this.val($(action.form).find('select:eq(' + i + ')').val());
	    
	    if($this.val().length === 0)
	    {
		$this.remove();
	    }
	});
	
	$newForm.find('#usuario-perfis-all').remove();
	
	action.form = $newForm[0];
	
	action.onError = function(data)
	{
	    if(data.message === 'USUARIO_STATUS_INATIVO')
	    {
		return window.location = ROOT + '/autenticacao/logout';
	    }
	};
	
	action.onSuccess = function(data)
	{
	    if(USUARIO_NEW === false)
	    {
		if(USUARIO_ID === data.data.usuarioId)
		{
		    // this one has preference over the rest
		    // in case of getting in here :-)
		    if(data.data.status === 'INATIVO')
		    {
			load(window.location = ROOT + '/autenticacao/logout');
		    }
		    
		    if($.inArray('ESTABELECIMENTO_USUARIO_LEITURA', data.data.perfis) === -1)
		    {
			load(window.location = ROOT + '/autenticacao');
		    }
		    
		    reload(700);
		}
	    }
	    else
	    {
		load(ROOT + '/usuario/' + data.data.usuarioId, 700);
	    }
	};
    });
    
    $('[name="perfis"]').change(function()
    {
	$('#usuario-perfis-all').prop('checked', false);
    });
    
    $('#usuario-perfis-all').change(function()
    {
	$(this).parents('fieldset').find('[name="perfis"]:not([id="usuario-perfis-all"])').prop('checked', $(this).prop('checked'));
    });
});