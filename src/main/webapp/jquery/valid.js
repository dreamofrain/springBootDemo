$.extend($.fn.textbox.defaults.rules, {
    subjectA: {
        validator: function (value) {
          return /^\d{4}$/.test(value);
        },
        message: '一级科目编号为4位数字'
    },
    subjectB: {
          validator: function (value) {
            return /(^\d{4})-(\d{2}$)/.test(value);
          },
          message: '二级科目编号为4位数字-2位数字'
    },
    subjectC: {
        validator: function (value) {
          return /^[0-8]{1}\d{7}$|^9\d{6}$/.test(value);
        },
        message: '三级科目编号为以9开头的7位数字或不以9开头的8位数字'
    }
});
