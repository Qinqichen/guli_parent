package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.VO.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author qinqichen
 * @since 2022-03-15
 */
@Api( tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {


    @Autowired
    private EduTeacherService teacherService;


    @ApiOperation("所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){

        List<EduTeacher> eduTeachers = teacherService.list(null);


//        try {
//            int a = 1/0;
//        }catch (Exception e){
//            throw new GuliException(2001,"执行了自定义异常");
//        }

        return R.ok().data("items",eduTeachers);
    }

    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacher(@PathVariable("id")String id ){

        boolean result = teacherService.removeById(id);

        if (result) {
            return R.ok();
        }else {
            return R.error();
        }

    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTehcher/{page}/{limit}")
    public R pageListTeacher(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){

        Page<EduTeacher> pageParam = new Page<>(page, limit);
        teacherService.page(pageParam, null);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R teacherQuery(@PathVariable Long current,
                          @PathVariable Long limit,
                          @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> pageParam = new Page<>(current,limit);

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }

        teacherService.page(pageParam,wrapper);

        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R save(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){
        boolean save = teacherService.save(teacher);
        return save ? R.ok() : R.error();
    }

    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id ){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher t) {
        boolean b = teacherService.updateById(t);
        return b?R.ok():R.error();
    }

}

