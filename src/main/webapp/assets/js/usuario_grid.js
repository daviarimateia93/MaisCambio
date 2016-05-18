$(function()
{
    $('#change-senha-modal').on('show.bs.modal', function()
    {
        $('input', this).val('');
    });
    
    $('#change-senha-modal').on('shown.bs.modal', function()
    {
        $('input:first', this).focus();
    });
    
    $('#remove-modal').on('shown.bs.modal', function()
    {
        $('button:first', this).focus();
    });
    
    $('#change-senha-form').on('ajaxsubmit', function(e, action)
    {
        // we want to hide notification, in case of user click on the button,
        // and lose input focus - triggering blur event
        if(!testSenha(true, true, ($('.toast-error').length > 0)))
        {
            action.stop = true;
        }
        
        action.onSuccess = function(data)
        {
            $('#change-senha-modal').modal('hide');
        };
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
            load(ROOT + '/usuario', 0, e);
        },
        
        detail: function(e)
        {
            var data = Grid.fnGetData(Grid.find('.active-menu')[0] || Grid.find('.active')[0]);
            
            load(ROOT + '/usuario/' + data.usuarioId, 0, e);
        },
        
        changeSenha: function(e)
        {
            var data = Grid.fnGetData(Grid.find('.active-menu')[0] || Grid.find('.active')[0]);
            
            $('#change-senha-modal .modal-title .apelido').text(data.apelido);
            $('#change-senha-modal form').attr('action', ROOT + '/usuario/' + data.usuarioId + '/changeSenha');
            $('#change-senha-modal').modal('show');
        },
        
        remove: function(e)
        {
            var data = Grid.fnGetData(Grid.find('.active-menu')[0] || Grid.find('.active')[0]);
            
            if(data.apelido === USUARIO_APELIDO)
            {
                toastr.error('Usuário à ser excluído, não poder ser o mesmo do atual logado');
            }
            else
            {
                $('#remove-modal .modal-title .apelido').text(data.apelido);
                $('#remove-modal form').attr('action', ROOT + '/usuario/' + data.usuarioId + '/delete');
                $('#remove-modal').modal('show');
            }
        }
    };
    // End Actions
    
    // Grid
    Grid.ajaxSource = ROOT + '/usuario/search';
    
    Grid.onSelect = function(e)
    {
        Actions.detail(e);
    };
    
    Grid.columns = [{
        mData: 'usuarioId',
        sTitle: 'Código',
        sName: 'usuarioId',
        sSearchOperator: 'EQUALS'
    }, {
        mData: 'apelido',
        sTitle: 'Usuário',
        sName: 'apelido',
        sSearchOperator: 'ILIKE'
    }, {
        mData: 'pessoa.cnpj',
        sTitle: 'CNPJ',
        sName: 'pessoa.cnpj',
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
        mData: 'pessoa.cpf',
        sTitle: 'CPF',
        sName: 'pessoa.cpf',
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
        mData: 'pessoa.idEstrangeiro',
        sTitle: 'ID estrangeiro',
        sName: 'pessoa.idEstrangeiro',
        sSearchOperator: 'LIKE'
    }, {
        mData: 'status',
        sTitle: 'Status',
        sName: 'status',
        sSearchOperator: 'EQUALS',
        mRender: function(value)
        {
            return Utils.replace('usuario.status.' + value);
        },
        mSearch: function(value)
        {
            return Utils.replace('usuario.status.' + value, {
                insensitive: true
            });
        }
    }];
    
    Grid.gMenu.items = [{
        name: 'detail',
        title: 'Detalhes',
        iconClass: 'fa-eye fa-fw',
        onClick: Actions.detail
    }];
    
    if(hasPerfil('ESTABELECIMENTO_USUARIO_ESCRITA'))
    {
        Grid.gMenu.items.push({
            name: 'changeSenha',
            title: 'Alterar senha',
            iconClass: 'fa-unlock-alt fa-fw',
            onClick: Actions.changeSenha
        });
        
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
    
    if(hasPerfil('ESTABELECIMENTO_USUARIO_ESCRITA'))
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
    
    if(hasPerfil('ESTABELECIMENTO_USUARIO_ESCRITA'))
    {
        Grid.gToolbar.items.push({
            title: 'Alterar senha',
            name: 'changeSenha',
            type: Grid.gToolbar.type.GREEN,
            classes: ['disabled'],
            iconClass: 'fa-unlock-alt',
            onClick: Actions.changeSenha
        });
        
        Grid.gToolbar.items.push({
            title: 'Excluir',
            classes: ['disabled', 'remove'],
            name: 'remove',
            type: Grid.gToolbar.type.RED,
            iconClass: 'fa-trash-o',
            onClick: Actions.remove
        });
    }
    
    Grid.getToolbarButtonsFilterOnSelectTr = function($tr)
    {
        return Grid.fnGetData($tr[0]).usuarioId === USUARIO_ID ? '.disabled:not(.remove)' : '.disabled';
    };
    
    Grid.getMenuItemsOnShow = function($tr)
    {
        var data = Grid.fnGetData($tr[0]);
        
        var items = [];
        
        for(var i = 0, len = Grid.gMenu.items.length; i < len; i++)
        {
            var item = Grid.gMenu.items[i];
            
            if(data.usuarioId === USUARIO_ID)
            {
                if(item.name !== 'remove')
                {
                    items.push(item);
                }
            }
            else
            {
                items.push(item);
            }
        }
        
        return items;
    };
    
    Grid.init();
    // End Grid
});