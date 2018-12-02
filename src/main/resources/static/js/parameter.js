


//parameter_add 初始化表单验证
function parameterInitCheck(id) {
    $('#defaultForm')
        .bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                name: {
                    message: '不是有效的用户名',
                    validators: {
                        notEmpty: {
                            message: '该元素不能为空'
                        }
                    }
                },
                code: {
                    validators: {
                        notEmpty: {
                            message: 'code不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]+$/,
                            message: '必须为数字'
                        },
                        remote: {
                            url: '/system/parameter_code_check',
                            message: '编码已存在',
                            delay:1500,
                            type:'POST'
                        }
                    }
                }
            }
        })
        .on('success.form.bv', function(e) {
            // 阻止默认提交事件
            e.preventDefault();

            // 获取form表单
            var $form = $(e.target);

            // Get the BootstrapValidator instance
            var bv = $form.data('bootstrapValidator');

            var idResult = "dataTable";
            if(id){
                idResult = id;
            }
            $.ajax({
                url: $form.attr('action'),
                type:"post",
                data:$form.serialize(),
                dataType:'json',
                success:function (result) {
                    if(result.type == "alert"){
                        alert(result.message);
                    }
                    $("#modal").modal("hide");
                    $("#"+idResult).bootstrapTable("selectPage",1);
                    $("#"+idResult).bootstrapTable(('refresh'));
                },
                error:function () {
                    console.log("error");
                }
            });
        });
}

