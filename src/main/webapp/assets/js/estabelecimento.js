$(function()
{
    $('#estabelecimento-form').on('ajaxsubmit', function(e, action)
    {
	var $newForm = $(action.form).clone();
	
	$newForm.find('[data-mask]').each(function()
	{
	    $(this).val($(this).val().replace(/\D/g, ''))
	});
	
	$newForm.find('select').each(function()
	{
	    $(this).val($(action.form).find('#' + $(this).attr('id')).val()).change();
	});
	
	var $razaoSocial = $newForm.find('input[name="razaoSocial"]');
	
	if($razaoSocial.length > 0)
	{
	    if($razaoSocial.val().length == 0)
	    {
		$razaoSocial.remove();
	    }
	}
	
	fixPessoaForm($newForm);
	
	action.form = $newForm[0];
	
	action.onSuccess = function(data)
	{
	    load(ROOT + '/estabelecimento?success=true');
	};
    });
    
    $('#estabelecimento-activate').click(function()
    {
	load(ROOT + '/estabelecimento/' + PESSOA_ID + '/activate');
    });
});