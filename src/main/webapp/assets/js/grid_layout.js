$(function()
{
    function selectTr(tr)
    {
        var $tr = $(tr);
        
        if($tr.parents('thead').length === 0 && $tr.find(' > .details').length === 0 && $tr.parents('.details').length === 0)
        {
            var filter = '.disabled';
            
            if(typeof Grid.getToolbarButtonsFilterOnSelectTr === 'function')
            {
                filter = Grid.getToolbarButtonsFilterOnSelectTr($tr);
            }
            
            $tr.addClass('active').siblings().removeClass('active').siblings('.active-details').each(function()
            {
                if(!$(this).hasClass('active'))
                {
                    $(this).removeClass('active-details');
                    
                    Grid.fnClose(this);
                }
            });
            
            Grid.gToolbar.getButtons().filter('.was-disabled').removeClass('was-disabled').addClass('disabled');
            Grid.gToolbar.getButtons().filter(filter).removeClass('disabled').addClass('was-disabled');
        }
    }
    
    function isEmpty()
    {
        return Grid.find('.dataTables_empty').length > 0;
    }
    
    // Grid
    Grid.sorting = [[0, 'desc']];
    
    Grid.disableSelection = true;
    
    Grid.onSelect = null;
    
    Grid.onTrClick = function()
    {
        if(!isEmpty() && !Grid.gMenu.isVisible())
        {
            selectTr(this);
        }
    };
    
    Grid.onTrRightClick = function(e)
    {
        if(!isEmpty())
        {
            selectTr(this);
            
            if(Grid.gMenu.items.length > 0)
            {
                var $tr = $(this);
                
                if($tr.parents('thead').length === 0 && $tr.find(' > .details').length === 0 && $tr.parents('.details').length === 0)
                {
                    Grid.gMenu.show(this, e);
                }
            }
        }
    };
    
    Grid.onAjaxLoad = function()
    {
        Grid.gToolbar.getButtons().filter('.was-disabled').removeClass('was-disabled').addClass('disabled');
    };
    
    Grid.onArrowDown = function()
    {
        if(!isEmpty() && !Grid.gMenu.isVisible())
        {
            var $active = Grid.find('tbody tr.active');
            
            if($active.length === 0 || $active.is(':last-child'))
            {
                selectTr(Grid.find('tbody tr:first'));
            }
            else
            {
                selectTr($active.next());
            }
        }
    };
    
    Grid.onArrowUp = function()
    {
        if(!isEmpty() && !Grid.gMenu.isVisible())
        {
            var $active = Grid.find('tbody tr.active');
            
            if($active.length === 0 || $active.is(':first-child'))
            {
                selectTr(Grid.find('tbody tr:last'));
            }
            else
            {
                selectTr($active.prev());
            }
        }
    };
    
    Grid.onArrowLeft = function()
    {
        $('#grid .dataTables_paginate [class$="previous"]').click();
    };
    
    Grid.onArrowRight = function()
    {
        $('#grid .dataTables_paginate [class$="next"]').click();
    };
    
    Grid.onTrDoubleClick = function(e)
    {
        if(typeof Grid.onSelect === 'function')
        {
            Grid.onSelect(e);
        }
    };
    
    Grid.onEnter = function()
    {
        if(!isEmpty() && !Grid.gMenu.isVisible())
        {
            Grid.find('tbody tr.active').trigger('dblclick.grid');
        }
    };
    
    var gMenuAuxItems = [];
    
    Grid.gMenu.onShow = function(tr)
    {
        if(typeof Grid.getMenuItemsOnShow === 'function')
        {
            for(var i = 0, len = Grid.gMenu.items.length; i < len; i++)
            {
                gMenuAuxItems[i] = Grid.gMenu.items[i];
            }
            
            Grid.gMenu.items = Grid.getMenuItemsOnShow($(tr));
        }
    };
    
    Grid.gMenu.onHidden = function(tr)
    {
        if(typeof Grid.getMenuItemsOnShow === 'function')
        {
            Grid.gMenu.items = [];
            
            for(var i = 0, len = gMenuAuxItems.length; i < len; i++)
            {
                Grid.gMenu.items[i] = gMenuAuxItems[i];
            }
        }
    }
    // End Grid
    
    function buscaFocus(e)
    {
        $('#grid .dataTables_filter input').focus();
        
        if(e !== undefined)
        {
            e.preventDefault();
            e.stopPropagation();
            
            return false;
        }
    }
    
    function toolbarFocus(e)
    {
        $('#grid .toolbar button:first').focus();
        
        if(e !== undefined)
        {
            e.preventDefault();
            e.stopPropagation();
            
            return false;
        }
    }
    
    function lengthFocus(e)
    {
        $('#grid .dataTables_length select').focus();
        
        if(e !== undefined)
        {
            e.preventDefault();
            e.stopPropagation();
            
            return false;
        }
    }
    
    $(document).on('keydown', null, 'alt+shift+1', buscaFocus);
    $(document).on('keydown', null, 'alt+shift+2', toolbarFocus);
    $(document).on('keydown', null, 'alt+shift+3', lengthFocus);
});