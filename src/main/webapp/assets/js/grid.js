var Grid = {
    
    __table: null,
    __lastAjaxRequest: null,
    displayLength: 10,
    disableSelection: false,
    sorting: [[0, 'desc']],
    ajaxSource: null,
    ajaxDataProp: 'content',
    ajaxDataPreffix: null,
    columns: null,
    processing: true,
    serverSide: true,
    onTrClick: null,
    onTdClick: null,
    onThClick: null,
    onTrDoubleClick: null,
    onTdDoubleClick: null,
    onThDoubleClick: null,
    onTrRightClick: null,
    onTdRightClick: null,
    onThRightClick: null,
    onArrowDown: null,
    onArrowUp: null,
    onArrowLeft: null,
    onArrowRight: null,
    onAjaxLoad: null,
    onDraw: null,
    header: true,
    footer: true,
    calculateWidth: false,
    language: {
        
        sProcessing: 'Carregando...',
        sLengthMenu: 'Exibir _MENU_ registros',
        sZeroRecords: 'Nenhum resultado encontrado',
        sInfo: 'Exibindo de _START_ até _END_ de _TOTAL_ registro(s)',
        sInfoEmpty: 'Exibindo de 0 até 0 de 0 registro(s)',
        sInfoFiltered: '(filtrado de _MAX_ registro(s) no total)',
        sInfoPostFix: '',
        sSearch: 'Buscar: ',
        sUrl: '',
        oPaginate: {
            
            sFirst: 'Primeiro',
            sPrevious: 'Anterior',
            sNext: 'Próximo',
            sLast: 'Último',
        }
    },
    
    init: function()
    {
        var self = this;
        
        var dom;
        
        if(this.header === true && this.footer === false)
        {
            dom = 'r<"dataTables_title"><"dataTables_header"lf<"clear"><"toolbar">><"dataTables_scroll"t><"dataTables_footer"<"clear">';
        }
        else if(this.header === false && this.footer === true)
        {
            dom = 'r<"dataTables_title"><"dataTables_header"<"clear"><"toolbar">><"dataTables_scroll"t><"dataTables_footer"ip<"clear">';
        }
        else if(this.header === false && this.footer == false)
        {
            dom = 'r<"dataTables_title"><"dataTables_header"<"clear"><"toolbar">><"dataTables_scroll"t><"dataTables_footer"<"clear">';
        }
        else
        {
            dom = 'r<"dataTables_title"><"dataTables_header"lf<"clear"><"toolbar">><"dataTables_scroll"t><"dataTables_footer"ip<"clear">';
        }
        
        if(this.columns !== null)
        {
            for(var i = 0, len = this.columns.length; i < len; i++)
            {
                if(typeof this.columns[i]['mRender'] !== 'function')
                {
                    this.columns[i]['mRender'] = function(value, type, full)
                    {
                        return Utils.format.html(value);
                    };
                }
            }
            
            this.__table = $('#grid > table').dataTable({
                iDisplayLength: self.displayLength,
                bServerSide: self.serverSide,
                aaSorting: self.sorting,
                sDom: dom,
                sAjaxDataProp: self.ajaxDataProp,
                sAjaxSource: self.ajaxSource,
                aoColumns: self.columns,
                bProcessing: self.processing,
                oLanguage: self.language,
                fnDrawCallback: function(oSettings)
                {
                    if(typeof self.onDraw === 'function')
                    {
                        self.onDraw(oSettings);
                    }
                },
                fnServerData: function(sSource, aoData, fnCallback, oSettings)
                {
                    var undefined;
                    
                    var filter;
                    
                    if(self.serverSide)
                    {
                        var starterIndex = oSettings._iDisplayStart;
                        var endIndex = starterIndex + oSettings._iDisplayLength;
                        var filters = {};
                        
                        for(var i = 0, len = oSettings.aaSorting.length; i < len; i++)
                        {
                            if(oSettings.aoColumns[oSettings.aaSorting[i][0]].sName !== undefined && oSettings.aoColumns[oSettings.aaSorting[i][0]].sName !== '')
                            {
                                var searchOperator = oSettings.aoColumns[oSettings.aaSorting[i][0]].sSearchOperator;
                                searchOperator = searchOperator !== undefined ? searchOperator : 'LIKE';
                                
                                filters[oSettings.aoColumns[oSettings.aaSorting[i][0]].mData] = {
                                    name: oSettings.aoColumns[oSettings.aaSorting[i][0]].sName,
                                    order: oSettings.aaSorting[i][1].toUpperCase(),
                                    operator: searchOperator,
                                    glue: 'OR'
                                };
                            }
                        }
                        
                        for(var i = 0, len = oSettings.aoColumns.length; i < len; i++)
                        {
                            var searchOperator = oSettings.aoColumns[i].sSearchOperator;
                            searchOperator = searchOperator !== undefined ? searchOperator : 'LIKE';
                            
                            if(filters[oSettings.aoColumns[i].sName] === undefined && oSettings.aoColumns[i].sName !== undefined && oSettings.aoColumns[i].sName !== '')
                            {
                                filters[oSettings.aoColumns[i].mData] = {
                                    name: oSettings.aoColumns[i].sName,
                                    operator: searchOperator,
                                    glue: 'OR'
                                };
                            }
                            
                            if(typeof oSettings.aoColumns[i].mSearch === 'function')
                            {
                                filters[oSettings.aoColumns[i].mData].search = oSettings.aoColumns[i].mSearch;
                            }
                        }
                        
                        for(index in filters)
                        {
                            var value = filters[index].search !== undefined ? filters[index].search(oSettings.oPreviousSearch.sSearch) : oSettings.oPreviousSearch.sSearch;
                            
                            if(value === undefined)
                            {
                                filters[index].value = '';
                            }
                            else
                            {
                                filters[index].value = (filters[index].operator === 'LIKE' || filters[index].operator === 'ILIKE') && value.length > 0 ? '%' + value + '%' : value;
                            }
                            
                            filters[index]['nullSuppressed'] = true;
                            filters[index]['emptySuppressed'] = true;
                        }
                        
                        filter = 'repositoryQueryStarterIndex=' + encodeURIComponent(starterIndex) + '&repositoryQueryEndIndex=' + encodeURIComponent(endIndex);
                        
                        for(var i = 0, len = fixedFilters.length; i < len; i++)
                        {
                            filter += '&repositoryQueryFilter=' + encodeURIComponent(fixedFilters[i] + 'fixed=true;');
                        }
                        
                        for( var index in filters)
                        {
                            filter += '&repositoryQueryFilter=';
                            
                            var filterBuilder = '';
                            
                            for( var subIndex in filters[index])
                            {
                                if(subIndex !== 'search')
                                {
                                    filterBuilder += subIndex + '=' + filters[index][subIndex] + ';';
                                }
                            }
                            
                            filterBuilder.substring(0, filterBuilder.length - 1);
                            
                            filter += encodeURIComponent(filterBuilder);
                        }
                        
                        for(var i = 0, len = aoData.length; i < len; i++)
                        {
                            if(aoData[i].name === 'sEcho')
                            {
                                filter += '&sEcho=' + aoData[i].value;
                                break;
                            }
                        }
                    }
                    
                    if(Grid.__lastAjaxRequest !== null)
                    {
                        Grid.__lastAjaxRequest.abort();
                    }
                    
                    var ajaxData = filter;
                    
                    if(typeof self.getAjaxData === 'function')
                    {
                        ajaxData = self.getAjaxData.call(this, ajaxData);
                    }
                    
                    Grid.__lastAjaxRequest = oSettings.jqXHR = $.ajax({
                        dataType: 'JSON',
                        type: 'GET',
                        url: sSource,
                        data: ajaxData,
                        success: function(data, statusText, jqXHR)
                        {
                            var dataDT = data;
                            
                            if(self.ajaxDataPreffix !== null)
                            {
                                dataDT = eval('data.' + self.ajaxDataPreffix);
                            }
                            
                            fnCallback.call(this, dataDT, statusText, jqXHR)

                            if(typeof self.onAjaxLoad === 'function')
                            {
                                self.onAjaxLoad.call(this, data, statusText, jqXHR);
                            }
                        },
                        error: function(jqXHR, statusText, errorThrown)
                        {
                            if(statusText === 'timeout')
                            {
                                $('#grid .dataTables_processing').css('visibility', 'hidden');
                                
                                toastr.error('Por favor, tente novamente');
                            }
                        }
                    });
                }
            });
            
            for( var index in this.__table)
            {
                this[index] = this.__table[index];
            }
            
            var fnOpen = this.fnOpen;
            
            this['fnOpen'] = function(a, b)
            {
                fnOpen.call(this, a, b, 'details');
            }
        }
        
        if(this.disableSelection)
        {
            Utils.textSelection.disable($('#grid table'));
        }
        
        if($.trim(title).length > 0)
        {
            $('#grid .dataTables_title').append('<h4 class="text-center">' + title + '</h4>');
        }
        else
        {
            $('#grid .dataTables_processing').addClass('center');
        }
        
        if(!this.calculateWidth)
        {
            $('#grid thead th').each(function()
            {
                $(this).css('width', '');
            });
        }
        
        $('#grid thead th').each(function(i)
        {
            var column = self.columns[i];
            var $this = $(this);
            
            if(typeof column.oClasses === 'object')
            {
                for(var iClasses = 0, iClassesLen = column.oClasses.length; iClasses < iClassesLen; iClasses++)
                {
                    $this.addClass(column.oClasses[iClasses]);
                }
            }
        });
        
        this.__bindEvents();
        
        this.gMenu.__init();
        
        this.gToolbar.__init();
    },
    
    __bindEvents: function()
    {
        var self = this;
        
        $(document).off('contextmenu.grid', '#grid table');
        $(document).on('contextmenu.grid', '#grid table', function(e)
        {
            var $target = $(e.target);
            
            if($target.parents('#grid').length > 0)
            {
                var tagName = $target.prop('tagName').toUpperCase();
                
                if(tagName !== 'TR' && tagName !== 'TD' && tagName !== 'TH')
                {
                    if($target.parents('td').length > 0)
                    {
                        tagName = 'TD';
                    }
                    else if($target.parents('th').length > 0)
                    {
                        tagName == 'TH';
                    }
                    else if($target.parents('td').length > 0)
                    {
                        tagName == 'TD';
                    }
                }
                
                if(tagName === 'TR')
                {
                    if(self.onTrRightClick !== null)
                    {
                        self.onTrRightClick.call(e.target, e);
                    }
                }
                
                if(tagName === 'TD')
                {
                    if($target.parents('tr').length > 0 && self.onTrRightClick !== null)
                    {
                        self.onTrRightClick.call($target.parents('tr')[0], e);
                    }
                    
                    if(self.onTdRightClick !== null)
                    {
                        self.onTdRightClick.call(e.target, e);
                    }
                }
                
                if(tagName === 'TH')
                {
                    if($target.parents('tr').length > 0 && self.onTrRightClick !== null)
                    {
                        self.onTrRightClick.call($target.parents('tr')[0], e);
                    }
                    
                    if(self.onThRightClick !== null)
                    {
                        self.onThRightClick.call(e.target, e);
                    }
                }
            }
            
            return false;
        });
        
        $(document).off('click.grid', '#grid table tr');
        $(document).off('tap.grid', '#grid table tr');
        
        if(typeof self.onTrClick === 'function')
        {
            __gridTrClicked = false;
            
            $(document).on('click.grid', '#grid table tr', function(e)
            {
                if(e.button === 0)
                {
                    __gridTrClicked = true;
                    self.onTrClick.call(this, e);
                }
            });
            
            $(document).on('tap.grid', '#grid table tr', function(e)
            {
                if((e.button === 0 || e.button === undefined) && __gridTrClicked === false)
                {
                    self.onTrClick.call(this, e);
                }
            });
        }
        
        $(document).off('click.grid', '#grid table td');
        $(document).off('tap.grid', '#grid table td');
        
        if(typeof self.onTdClick === 'function')
        {
            __gridTdClicked = false;
            
            $(document).on('click.grid', '#grid table td', function(e)
            {
                if(e.button === 0)
                {
                    __gridTdClicked = true;
                    self.onTdClick.call(this, e);
                }
            });
            
            $(document).on('tap.grid', '#grid table td', function(e)
            {
                if((e.button === 0 || e.button === undefined) && __gridTdClicked === false)
                {
                    self.onTdClick.call(this, e);
                }
            });
        }
        
        $(document).off('click.grid', '#grid table th');
        $(document).off('tap.grid', '#grid table th');
        
        if(typeof self.onThClick === 'function')
        {
            $(document).on('click.grid', '#grid table th', self.onThClick);
            $(document).on('tap.grid', '#grid table th', self.onThClick);
        }
        
        $(document).off('dblclick.grid', '#grid table tr');
        $(document).off('dbltap.grid', '#grid table tr');
        
        if(typeof self.onTrDoubleClick === 'function')
        {
            $(document).on('dblclick.grid', '#grid table tr', self.onTrDoubleClick);
            $(document).on('dbltap.grid', '#grid table tr', self.onTrDoubleClick);
        }
        
        $(document).off('dblclick.grid', '#grid table td');
        $(document).off('dbltap.grid', '#grid table td');
        
        if(typeof self.onTdDoubleClick === 'function')
        {
            $(document).on('dblclick.grid', '#grid table td', self.onTdDoubleClick);
            $(document).on('dbltap.grid', '#grid table td', self.onTdDoubleClick);
        }
        
        $(document).off('dblclick.grid', '#grid table th');
        $(document).off('dbltap.grid', '#grid table th');
        
        if(typeof self.onThDoubleClick === 'function')
        {
            $(document).on('dblclick.grid', '#grid table th', self.onThDoubleClick);
            $(document).on('dbltap.grid', '#grid table th', self.onThDoubleClick);
        }
        
        $(document).off('keydown.grid', null);
        $(document).off('keyup.grid', null);
        
        if(typeof self.onArrowDown === 'function')
        {
            $(document).on('keydown.grid', null, 'alt+shift+down', self.onArrowDown);
        }
        
        if(typeof self.onArrowUp === 'function')
        {
            $(document).on('keydown.grid', null, 'alt+shift+up', self.onArrowUp);
        }
        
        if(typeof self.onArrowLeft === 'function')
        {
            $(document).on('keydown.grid', null, 'alt+shift+left', self.onArrowLeft);
        }
        
        if(typeof self.onArrowRight === 'function')
        {
            $(document).on('keydown.grid', null, 'alt+shift+right', self.onArrowRight);
        }
        
        if(typeof self.onEnter === 'function')
        {
            $(document).on('keydown.grid', null, 'alt+shift+return', self.onEnter);
        }
    },
    
    gToolbar: {
        
        __init: function()
        {
            if(this.items.length === 0)
            {
                $('#grid .toolbar').addClass('hide');
            }
            else
            {
                var self = this;
                
                var undefined;
                
                var HTML = '';
                
                for(var i = 0, len = this.items.length; i < len; i++)
                {
                    var item = this.items[i];
                    
                    var typeAsClass = item.type || Grid.gToolbar.type.DEFAULT;
                    
                    var classes = '';
                    
                    if(item.classes != undefined)
                    {
                        classes = item.classes.length > 0 ? item.classes.join(' ') : '';
                    }
                    
                    HTML += '<button name="' + item.name + '" class="btn ' + typeAsClass + ' ' + classes + '">';
                    
                    if(item.iconClass !== undefined)
                    {
                        HTML += '<span class="fa glyphicon ' + item.iconClass + '"></span> ';
                    }
                    
                    HTML += item.title;
                    HTML += '</button>';
                }
                
                $('#grid .toolbar').removeClass('hide').html(HTML);
                $('#grid .toolbar button').click(function(e)
                {
                    var index = $(this).index();
                    
                    if(typeof self.items[index].onClick === 'function')
                    {
                        self.items[index].onClick.call(this, e);
                    }
                    else
                    {
                        window.location = self.items[index].href;
                    }
                });
            }
        },
        
        // expected: { title: '', name: '', [classes: []], [type:
        // Grid.gToolbar.type.DEFAULT], onClick: function(){} || href: '',
        // [iconClass: ''] }
        items: [],
        
        getButtons: function()
        {
            return $('#grid .toolbar button');
        }
    },
    
    gMenu: {
        
        __el: null,
        onShow: null,
        onShown: null,
        onHide: null,
        onHidden: null,
        
        __init: function()
        {
            this.__bindEvents();
        },
        
        __bindEvents: function()
        {
            var self = this;
            
            $(document).off('mousedown.grid', '#grid-right-menu-mask');
            $(document).on('mousedown.grid', '#grid-right-menu-mask', function(e)
            {
                var $el = $(e.target);
                
                if(($el.prop('tagName').toUpperCase() === 'A' || $el.prop('tagName').toUpperCase() === 'SPAN') && $el.parents('#grid-right-menu').length > 0)
                {
                    var item = self.items[$el.parents('li').index()];
                    
                    self.hide();
                    
                    if(typeof item.onClick === 'function')
                    {
                        item.onClick.call(e.target, e);
                    }
                    else
                    {
                        window.location = $el.parents('li').find('a').attr('href');
                    }
                }
                else
                {
                    self.hide();
                }
            });
            
            $(window).off('resize.grid');
            $(window).on('resize.grid', function()
            {
                self.hide();
            })
        },
        
        isVisible: function()
        {
            return $('#grid-right-menu-mask').is(':visible');
        },
        
        show: function(el, e)
        {
            this.__el = el;
            var $el = $(el);
            var x = e.clientX;
            var y = e.clientY + $(window).scrollTop();
            var data = Grid.fnGetData(el);
            
            if(typeof this.onShow === 'function')
            {
                this.onShow(el);
            }
            
            $el.addClass('active-menu');
            
            var undefined;
            
            var $menu = $('#grid-right-menu');
            
            $menu.find('li.action').remove();
            
            var HTML = '';
            
            for(var i = 0, len = this.items.length; i < len; i++)
            {
                var item = this.items[i];
                
                HTML += '<li class="action" name="' + item.name + '">';
                HTML += '<a href="' + (item.href !== undefined ? item.href : 'javascript: void(0);') + '" tabindex="-1">';
                
                if(item.iconClass !== undefined)
                {
                    HTML += '<span class="fa glyphicon ' + item.iconClass + '"></span> ';
                }
                
                HTML += item.title;
                
                HTML += '</a>';
                HTML += '</li>';
            }
            
            $('#grid-right-menu-mask').css({
                display: 'block'
            }).appendTo('body');
            
            $menu.append(HTML).css({
                left: x,
                top: y
            });
            
            var width = $menu.outerWidth();
            var height = $menu.outerHeight();
            var windowWidth = $(window).outerWidth();
            var windowHeight = $(window).outerHeight();
            
            if(e.pageX - $(window).scrollLeft() + width > windowWidth)
            {
                $menu.css({
                    left: '-=' + width
                });
            }
            
            if(e.pageY - $(window).scrollTop() + height > windowHeight)
            {
                $menu.css({
                    top: '-=' + height
                });
            }
            
            if(typeof this.onShown === 'function')
            {
                this.onShown(el);
            }
        },
        
        hide: function()
        {
            if(typeof this.onHide === 'function')
            {
                this.onHide(this.__el);
            }
            
            $('.active-menu').removeClass('active-menu');
            $('#grid-right-menu-mask').css({
                display: 'none'
            });
            
            if(typeof this.onHidden === 'function')
            {
                this.onHidden(this.__el);
            }
        },
        
        // expected: { href: '' || onClick: '', title: '', [iconClass: ''],
        // name: '' }
        items: []
    }
};

Grid.gToolbar.type = {
    DEFAULT: 'btn-default',
    PRIMARY: 'btn-primary',
    SUCCESS: 'btn-success',
    INFO: 'btn-info',
    WARNING: 'btn-warning',
    DANGER: 'btn-danger',
    BLUE: 'btn-blue',
    GREEN: 'btn-green',
    ORANGE: 'btn-orange',
    RED: 'btn-red',
    PURPLE: 'btn-purple',
    PINK: 'btn-pink'
};
