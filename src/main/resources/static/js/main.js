//菜单栏样式
$(function () {
    var currentUrl = window.location;
    $(".sub_menu").each(function () {
        var url = $(this).children("a").attr("href");
        if(currentUrl.toString().indexOf(url) > 0 ){
            $(this).addClass("active");
            $(this).parent().removeClass("nav-hide");
            $(this).parent().addClass("nav-show");
            $(this).parent().css("display","block");
        }
    })
})

//主页面获取表格
function getTable (dataFormat,id) {
    $("#"+id).bootstrapTable({
        method:"get",
        sidePagination:"server",
        dataType: "json",
        toolbar: '#toolbar',
        queryParams: function queryParams(params) {   //设置查询参数
            var param = {
                pageNum: params.offset/params.limit
            };
            $(".search-data").each(function () {
                var key = $(this).attr("name");
                var val = $(this).val();
                param[key] = val;
            });
            console.log(param);
            return param;
        },
        columns:dataFormat
    });
}
//搜索按钮
$("#search-btn").click(function () {
    // $("#dataTable").bootstrapTable("selectPage",1).bootstrapTable("refresh");
    $("#dataTable").bootstrapTable("selectPage",1);
});

//模态框 清除缓存
$(document).on('hidden.bs.modal','.modal',function(e){
    $(this).find(".modal-content").empty();
    $(this).removeData("bs.modal");
});

//删除按钮
function delDatas(url,id) {
    var idResult = "dataTable";
    if(id){
        idResult = id;
    }
    var table = $("#"+idResult).bootstrapTable('getSelections');
    var ids = [];
    for(var i = 0; i < table.length;i++){
        ids.push(table[i].id)
    }
    $.ajax({
        url:url,
        type:'post',
        traditional:true,
        data:{ids:ids},
        dataType:'json',
        success:function(result){
            if(result.type == "alert"){
                alert(result.message);
            }
            $("#"+idResult).bootstrapTable("selectPage",1);
        },
        error:function () {
            alert("删除失败");
        }
    });
}


//预览按钮
function reviewData(url,field,id) {
    var idResult = "dataTable";
    if(id){
        idResult = id;
    }
    var table = $("#"+idResult).bootstrapTable('getSelections');
    if(table.length == 0){
        alert("请选择一条记录");
        return false;
    }else if(table.length > 1){
        alert("只能选择一条记录");
        return false;
    }
    var result = table[0][field];

    window.open(url + "?"+field+"="+result+"&preview=true");
}

//修改按钮
function editData(url,field,id) {
    var idResult = "dataTable";
    if(id){
        idResult = id;
    }
    var table = $("#"+idResult).bootstrapTable('getSelections');
    if(table.length == 0){
        alert("请选择一条记录");
        return false;
    }else if(table.length > 1){
        alert("只能选择一条记录");
        return false;
    }
    var result = table[0][field];

    $("#modal").modal({
        remote:url+ "?"+field+"="+result
    });
}

//初始化表单验证
function initCheck() {
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
                            message: '用户名不能为空'
                        },
                        stringLength: {
                            min: 1,
                            max: 30,
                            message: '用户名不能超过30个字符'
                        }
                        /*remote: {
                            url: 'remote.php',
                            message: 'The username is not available'
                        },*/
                        // regexp: {
                        //     regexp: /^[a-zA-Z0-9_\.]+$/,
                        //     message: ''
                        // }
                    }
                },
                email: {
                    validators: {
                        notEmpty: {
                            message: '邮箱不能为空'
                        },
                        emailAddress: {
                            message: '不是标准的邮箱地址'
                        }
                    }
                },
                identity: {
                    validators: {
                        notEmpty: {
                            message: '身份不能为空'
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        }
                    }
                },
                code: {
                    validators: {
                        notEmpty: {
                            message: 'CODE不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]+$/,
                            message: '必须为数字'
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
            console.log($('#defaultForm').serialize());

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
                    //刷新跳转至第一页
                    // $("#dataTable").bootstrapTable("selectPage",1).bootstrapTable("refresh");
                    $("#dataTable").bootstrapTable("selectPage",1);
                },
                error:function () {
                    console.log("error")
                }
            });
            // $.post($form.attr('action'), $form.serialize(), function(result) {
            //     console.log(result);
            // }, 'text');
        });
}

//格式化时间戳
function formatTimeStrap(time) {
    var date = new Date(time);
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = date.getDate() + ' ';
    h = (date.getHours() <10 ? '0'+date.getHours() : date.getHours() ) + ':';
    m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes() ) + ':';
    s = date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds();
    return Y+M+D+h+m+s;
}

//格式化性别
function formatSex(sex){
    if(sex == 1){
        return '男';
    }
    if(sex == 2){
        return '女';
    }
    return '保密';
}

// $(".multiselect").chosen();


// $.fn.serializeObject = function()
// {
//     var o = {};
//     var a = this.serializeArray();
//     $.each(a, function() {
//         if (o[this.name]) {
//             if (!o[this.name].push) {
//                 o[this.name] = [o[this.name]];
//             }
//             o[this.name].push(this.value || '');
//         } else {
//             o[this.name] = this.value || '';
//         }
//     });
//     return o;
// };



