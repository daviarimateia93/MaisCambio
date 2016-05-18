$(function()
{
    $(document).on('ajaxsubmit', '#taxa-form, #iof-compra-form, #iof-venda-form', function(e, action)
    {
        var $form = $(action.form);
        
        if($form.find('[name="status"]').length > 0)
        {
            $form.append('<input type="text" name="status" value="INATIVO" />');
        }
        
        $form.find('input[data-mask-money]').each(function()
        {
            var $this = $(this);
            
            $this.val($this.val().replace(/\./g, '').replace(/\,/g, '.').replace(/[^\d\.]+/g, ''));
        });
    });
});
