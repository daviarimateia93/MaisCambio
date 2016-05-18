var lastValidCep = null;
var loadCidadesDefaultCallback = null;

function loadCidades(estadoId, successCallback, $sltEstado)
{
    var $sltCidade = $sltEstado !== undefined && $sltEstado !== null ? $($sltEstado).parents('.form-group').find('#sltCidade, .sltCidade') : $('#sltCidade, .sltCidade');
    var $iptPais = $('#iptPais').length > 0 ? $('#iptPais') : $('.iptPais');
    var $emptyOption = $sltCidade.find('option:first').clone();
    var $selectedOption = $sltCidade.find('option:selected').clone();
    
    $.ajax({
        url: ROOT + '/cidade?estadoId=' + estadoId + '&paisId=' + $iptPais.val(),
        success: function(cidades)
        {
            var optionsHTML = '';
            
            for(var i = 0, len = cidades.length; i < len; i++)
            {
                optionsHTML += '<option value="' + Utils.format.html(cidades[i].cidadeId) + '">' + Utils.format.html(cidades[i].nome) + '</option>\r\n';
            }
            
            $sltCidade.empty().append($emptyOption).append(optionsHTML).val($selectedOption.val());
            
            if(typeof loadCidadesDefaultCallback === 'function')
            {
                loadCidadesDefaultCallback(cidades);
            }
            
            if(typeof successCallback === 'function')
            {
                successCallback(cidades);
            }
        }
    });
}

$(function()
{
    $('#iptCep, .iptCep').blur(function()
    {
        var $this = $(this);
        var cep = $this.val();
        
        if(/^[\d]{5}\-[\d]{3}$/.test(cep) && lastValidCep !== cep)
        {
            Utils.searchByCep(cep, function(endereco)
            {
                if(endereco.cidade != null)
                {
                    if(endereco.cidade.estado.estadoId !== $this.parents('.form-group').parent().find('#sltEstado, .sltEstado').val())
                    {
                        var $emptyOption = $this.parents('.form-group').parent().find('#sltCidade, .sltCidade').find('option:first').clone();
                        
                        $this.parents('.form-group').parent().find('#sltCidade, .sltCidade').empty().append($emptyOption).append('<option value="' + Utils.format.html(endereco.cidade.cidadeId) + '">' + Utils.format.html(endereco.cidade.nome) + '</option>').val(endereco.cidade.cidadeId).change();
                        
                        $this.parents('.form-group').parent().find('#sltEstado, .sltEstado').val(endereco.cidade.estado.estadoId).change();
                        
                        loadCidadesDefaultCallback = function()
                        {
                            $this.parents('.form-group').parent().find('#sltCidade, .sltCidade').val(endereco.cidade.cidadeId);
                        };
                    }
                    else
                    {
                        $this.parents('.form-group').parent().find('#sltCidade, .sltCidade').val(endereco.cidade.cidadeId).change();
                    }
                    
                    $this.parents('.form-group').parent().find('#iptBairro, .iptBairro').val(endereco.bairro);
                    $this.parents('.form-group').parent().find('#iptLogradouro, .iptLogradouro').val(endereco.logradouro);
                }
            });
            
            lastValidCep = cep;
        }
    });
    
    $('#sltEstado, .sltEstado').change(function()
    {
        var $this = $(this);
        
        loadCidades($this.val(), null, $this);
    });
});
