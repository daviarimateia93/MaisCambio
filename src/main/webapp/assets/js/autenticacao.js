$(function()
{
    $('#frmAutenticacao').on('ajaxsubmit', function(e, action)
    {
	var $this = $(this);
	
	action.onError = function(data, jqXHR)
	{
	    if(jqXHR.getResponseHeader('X-Uri').indexOf(ROOT + '/autenticacao') !== 0)
	    {
		load(jqXHR.getResponseHeader('X-Url'));
	    }
	    else if(jqXHR.status === 500)
	    {
		toastr.error('Erro no servidor');
		
		return false;
	    }
	};
    });
    
    var $estabelecimentoPessoaId = $('#frmAutenticacao [name="estabelecimentoPessoaId"]');
    
    if($.jStorage.get('ESTABELECIMENTO_PESSOA_ID') === null)
    {
	$estabelecimentoPessoaId.remove();
    }
    else
    {
	$estabelecimentoPessoaId.val($.jStorage.get('ESTABELECIMENTO_PESSOA_ID'));
    }
    
    if($.jStorage.get('AUTENTICACAO_APELIDO') !== null)
    {
	$('#frmAutenticacao [name="apelido"]').val($.jStorage.get('AUTENTICACAO_APELIDO'));
	$('#frmAutenticacao [name="remember"]').prop('checked', true);
	$('#frmAutenticacao [name="senha"]').focus();
    }
    else
    {
	$('#frmAutenticacao [name="remember"]').prop('checked', false);
	$('#frmAutenticacao [name="apelido"]').focus();
    }
    
    function tryToRemember()
    {
	var $apelido = $('#frmAutenticacao [name="apelido"]');
	var $remember = $('#frmAutenticacao [name="remember"]');
	
	var remember = $remember.prop('checked');
	
	if(remember)
	{
	    $.jStorage.set('AUTENTICACAO_APELIDO', $apelido.val());
	}
	else
	{
	    $.jStorage.set('AUTENTICACAO_APELIDO', null);
	}
    }
    
    $('#frmAutenticacao [name="apelido"]').keyup(tryToRemember);
    
    $('#frmAutenticacao [name="remember"]').change(tryToRemember);
});