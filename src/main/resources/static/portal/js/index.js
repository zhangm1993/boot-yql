/**
 * 判断波动解决方案图片的方法
 * @param n
 */
function move_img(n){
    if(n=='right'){
        var num=$(".on-show").removeClass("on-show").prev().addClass("on-show").length;
        if(num==0){
            $(".jk-a").last().addClass("on-show");
        }
    }else{
        var num= $(".on-show").removeClass("on-show").next().addClass("on-show").length;
        if(num==0){
            $(".jk-a").first().addClass("on-show");
        }
    }

}

/**
 * 添加到顶部的按钮
 */
function add_top(){
    $("body").append(
        `<button onclick=" $('html,body').animate({ scrollTop: 0 }, 1000)" class="fixed-bo w3-btn w3-hover-grey" style="display: none">
            <i class="fa fa-long-arrow-up"></i>
        </button>
        `
    );

    window.onscroll = function(){
        var scrollTop = $(document).scrollTop();
        if(scrollTop>$(document).height()*0.3){
            $(".fixed-bo").show();
        }else{
            $(".fixed-bo").hide();
        }
    }
}

/**
 * 判断样式
 * */
function num(n){

    if(n==null){
        return "s12";
    }
    var num=n.length;
    if(num>6){
        return "s1";
    }else if(num>4){
        return "s2";
    }else if(num==4){
        return "s3";
    }else if(num==3){
        return "s4";
    }else if(num==2){
        return "s6";
    }else if(num==1){
        return "s12";
    }
}

//分页提示显示
function page_font_com(data){

    var path=data.path;

    //共有多少个产品
    var totalElements=data.totalElements;
    //共有多少页
    var totalPages=data.totalPages;
    //每页最多显示多少条
    var size=data.size;
    //当前第几页
    var page=data.number+1;

    $(path).html("共有" + totalElements + "个产品,每页显示" +
        size + "个产品,共有" +totalPages + "页,当前第" + page + "页");

}