function testSenha(force, hideSuccessMessage, hideErrorMessage)
{
    var senha = $('#usuario-senha').val();
    var confirmation = $('#usuario-senha-confirmation').val();
    
    function senhaDoesNotMatch()
    {
        if(hideErrorMessage !== true)
        {
            toastr.error('Senha e confirmação não são iguais.');
        }
        
        return false;
    }
    
    function senhaDoesMatch()
    {
        if(hideSuccessMessage !== true)
        {
            toastr.success('Senha confirmada!');
        }
        
        return true;
    }
    
    if((senha.length > 0 && confirmation.length > 0) || force === true)
    {
        if(senha === confirmation && senha.length > 0 && confirmation.length > 0)
        {
            return senhaDoesMatch();
        }
        else
        {
            return senhaDoesNotMatch();
        }
    }
}

$(function()
{
    $('#usuario-senha, #usuario-senha-confirmation').blur(function()
    {
        testSenha();
    });
});