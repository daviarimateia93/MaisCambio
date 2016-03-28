function fixPessoaForm($form)
{
    var $telefone2 = $form.find('input[name="telefone2"]');
    
    if($telefone2.length > 0)
    {
	if($telefone2.val().length === 0)
	{
	    $telefone2.remove();
	}
    }
    
    var $email = $form.find('input[name="email"]');
    
    if($email.length > 0)
    {
	if($email.val().length == 0)
	{
	    $email.remove();
	}
    }
    
    var $idEstrangeiro = $form.find('input[name="idEstrangeiro"]');
    
    if($idEstrangeiro.length > 0)
    {
	if($idEstrangeiro.val().length == 0)
	{
	    $idEstrangeiro.remove();
	}
    }
    
    var $enderecoComplemento = $form.find('input[name="endereco.complemento"]');
    
    if($enderecoComplemento.length > 0)
    {
	if($enderecoComplemento.val().length === 0)
	{
	    $enderecoComplemento.remove();
	}
    }
}

var additionalSelectorsWhenCnpj = [];

$(function()
{
    $('#iptCnpj, .iptCnpj').focus(function()
    {
	$(this).parents('.form-group').find('#iptCpf, .iptCpf, #iptIdEstrangeiro, .iptIdEstrangeiro').attr('disabled', '').val('');
    });
    
    $('#iptCnpj, .iptCnpj').blur(function()
    {
	var $this = $(this);
	
	if(!/^[\d]{2}\.[\d]{3}\.[\d]{3}\/[\d]{4}\-[\d]{2}$/.test($(this).val()))
	{
	    $this.parents('.form-group').find('#iptCpf, .iptCpf, #iptIdEstrangeiro, .iptIdEstrangeiro').removeAttr('disabled');
	}
	else
	{
	    $this.parents('.form-group').find('#iptCpf, .iptCpf, #iptIdEstrangeiro, .iptIdEstrangeiro').attr('disabled', '');
	}
	
	if(/^[\d]{2}\.[\d]{3}\.[\d]{3}\/[\d]{4}\-[\d]{2}$/.test($(this).val()))
	{
	    $this.parents('.form-group').parent().find('#iptInscricaoEstadual, .iptInscricaoEstadual, #iptInscricaoSuframa, .iptInscricaoSuframa' + (additionalSelectorsWhenCnpj.length > 0 ? ',' + additionalSelectorsWhenCnpj.join(',') : '')).removeAttr('disabled');
	}
	else
	{
	    $this.parents('.form-group').parent().find('#iptInscricaoEstadual, .iptInscricaoEstadual, #iptInscricaoSuframa, .iptInscricaoSuframa' + (additionalSelectorsWhenCnpj.length > 0 ? ',' + additionalSelectorsWhenCnpj.join(',') : '')).attr('disabled', '').val('');
	}
    });
    
    $('#iptCpf, .iptCpf').focus(function()
    {
	$(this).parents('.form-group').parent().find('#iptCnpj, .iptCnpj, #iptIdEstrangeiro, .iptIdEstrangeiro, #iptInscricaoEstadual, .iptInscricaoEstadual, #iptInscricaoSuframa, .iptInscricaoSuframa' + (additionalSelectorsWhenCnpj.length > 0 ? ',' + additionalSelectorsWhenCnpj.join(',') : '')).attr('disabled', '').val('');
    });
    
    $('#iptCpf, .iptCpf').blur(function()
    {
	var $this = $(this);
	
	if(!/^[\d]{3}\.[\d]{3}\.[\d]{3}\-[\d]{2}$/.test($this.val()))
	{
	    $this.parents('.form-group').find('#iptCnpj, .iptCnpj, #iptIdEstrangeiro, .iptIdEstrangeiro').removeAttr('disabled');
	}
	else
	{
	    $this.parents('.form-group').find('#iptCnpj, .iptCnpj, #iptIdEstrangeiro, .iptIdEstrangeiro').attr('disabled', '');
	}
    });
    
    $('#iptIdEstrangeiro, .iptIdEstrangeiro').focus(function()
    {
	$(this).parents('.form-group').parent().find('#iptCnpj, .iptCnpj, #iptCpf, .iptCpf, #iptInscricaoEstadual, .iptInscricaoEstadual, #iptInscricaoSuframa, .iptInscricaoSuframa' + (additionalSelectorsWhenCnpj.length > 0 ? ',' + additionalSelectorsWhenCnpj.join(',') : '')).attr('disabled', '').val('');
    });
    
    $('#iptIdEstrangeiro, .iptIdEstrangeiro').blur(function()
    {
	var $this = $(this);
	
	if(!/^(.{5,20})$/.test($this.val()))
	{
	    $this.val('');
	    $this.parents('.form-group').find('#iptCnpj, .iptCnpj, #iptCpf, .iptCpf').removeAttr('disabled');
	}
	else
	{
	    $this.parents('.form-group').find('#iptCnpj, .iptCnpj, #iptCpf, .iptCpf').attr('disabled', '');
	}
    });
});
