$(function()
{
    $('#remove-modal').on('shown.bs.modal', function()
    {
        $('button:first', this).focus();
    });
    
    $('#remove-form').on('ajaxsubmit', function(e, action)
    {
        action.onError = function(data)
        {
            $('#remove-modal').modal('hide');
        };
        
        action.onSuccess = function(data)
        {
            $('#remove-modal').modal('hide');
            
            Grid.gMenu.hide();
            
            Actions.refresh();
            
            reload(700);
        };
    });
    
    // Actions
    Actions = {
        
        refresh: function(e)
        {
            Grid.fnReloadAjax();
        },
        
        insert: function(e)
        {
            load(ROOT + '/estabelecimento', 0, e);
        },
        
        detail: function(e)
        {
            var data = Grid.fnGetData(Grid.find('.active-menu')[0] || Grid.find('.active')[0]);
            
            load(ROOT + '/estabelecimento/' + data.pessoaId, 0, e);
        },
        
        remove: function(e)
        {
            var data = Grid.fnGetData(Grid.find('.active-menu')[0] || Grid.find('.active')[0]);
            
            $('#remove-modal .modal-title .nome-fantasia').text(data.nomeFantasia);
            $('#remove-modal form').attr('action', ROOT + '/estabelecimento/' + data.pessoaId + '/delete');
            $('#remove-modal').modal('show');
        }
    };
    // End Actions
    
    // Grid
    Grid.ajaxSource = ROOT + '/estabelecimento/search';
    
    Grid.onSelect = function(e)
    {
        Actions.detail(e);
    };
    
    Grid.columns = [{
        mData: 'pessoaId',
        sTitle: 'Código',
        sName: 'pessoaId',
        sSearchOperator: 'EQUALS'
    }, {
        mData: 'nomeFantasia',
        sTitle: 'Nome fantasia',
        sName: 'nomeFantasia',
        sSearchOperator: 'ILIKE'
    }, {
        mData: 'razaoSocial',
        sTitle: 'Razão social',
        sName: 'razaoSocial',
        sSearchOperator: 'ILIKE'
    }, {
        mData: 'cnpj',
        sTitle: 'CNPJ',
        sName: 'cnpj',
        sSearchOperator: 'LIKE',
        mSearch: function(value)
        {
            if(/^[\.\-\/\d]+$/.test(value))
            {
                value = value.replace(/\D+/g, '');
                
                if(value !== '')
                {
                    return value;
                }
            }
        },
        mRender: function(value, type, full)
        {
            return Utils.format.cnpj(value);
        }
    }, {
        mData: 'cpf',
        sTitle: 'CPF',
        sName: 'cpf',
        sSearchOperator: 'LIKE',
        mSearch: function(value)
        {
            if(/^[\.\-\d]+$/.test(value))
            {
                value = value.replace(/\D+/g, '');
                
                if(value !== '')
                {
                    return value;
                }
            }
        },
        mRender: function(value, type, full)
        {
            return Utils.format.cpf(value);
        }
    }, {
        mData: 'idEstrangeiro',
        sTitle: 'ID estrangeiro',
        sName: 'idEstrangeiro',
        sSearchOperator: 'ILIKE'
    }, {
        mData: 'telefone1',
        sTitle: 'Telefone',
        sName: 'telefone1',
        sSearchOperator: 'LIKE',
        mSearch: function(value)
        {
            if(/^[\+\(\)\ \d]+$/.test(value))
            {
                value = value.replace(/\D+/g, '');
                
                if(value !== '')
                {
                    return value;
                }
            }
        },
        mRender: function(value, type, full)
        {
            return Utils.format.telefone(value);
        }
    }];
    
    Grid.gMenu.items.push({
        name: 'detail',
        title: 'Detalhes',
        iconClass: 'fa-eye fa-fw',
        onClick: Actions.detail
    });
    
    if(hasPerfil('ESTABELECIMENTO_ESCRITA'))
    {
        Grid.gMenu.items.push({
            name: 'remove',
            title: 'Excluir',
            iconClass: 'fa-trash-o fa-fw',
            onClick: Actions.remove
        });
    }
    
    Grid.gToolbar.items = [{
        title: 'Atualizar',
        name: 'refresh',
        type: Grid.gToolbar.type.GREEN,
        iconClass: 'fa-refresh',
        onClick: Actions.refresh
    }];
    
    if(hasPerfil('ESTABELECIMENTO_ESCRITA'))
    {
        Grid.gToolbar.items.push({
            title: 'Novo',
            name: 'insert',
            type: Grid.gToolbar.type.GREEN,
            iconClass: 'glyphicon-plus',
            onClick: Actions.insert
        });
    }
    
    Grid.gToolbar.items.push({
        title: 'Detalhes',
        classes: ['disabled'],
        name: 'detail',
        type: Grid.gToolbar.type.GREEN,
        iconClass: 'fa-eye',
        onClick: Actions.detail
    });
    
    if(hasPerfil('ESTABELECIMENTO_ESCRITA'))
    {
        Grid.gToolbar.items.push({
            title: 'Excluir',
            classes: ['disabled'],
            name: 'remove',
            type: Grid.gToolbar.type.DANGER,
            iconClass: 'fa-trash-o',
            onClick: Actions.remove
        });
    }
    
    Grid.init();
    // End Grid
});