package org.csu.petstore.service.impl;

import org.csu.petstore.entity.Account;
import org.csu.petstore.entity.Profile;
import org.csu.petstore.entity.SignOn;
import org.csu.petstore.persistence.AccountMapper;
import org.csu.petstore.persistence.ProfileMapper;
import org.csu.petstore.persistence.SignOnMapper;
import org.csu.petstore.service.AccountService;
import org.csu.petstore.vo.AccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private SignOnMapper signOnMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public int signonForm(String username, String password) {
        try{
            SignOn signOn = signOnMapper.selectById(username);
            if(signOn.getPassword().equals(password)){
                return 0;
            }else{
                return 2;
            }
        }
        catch(Exception e){
            return 1;
        }
    }

    @Override
    public SignOn getSign(String username) {
        return signOnMapper.selectById(username);
    }

    @Override
    public AccountVO getAccount(String username) {
        Account account = accountMapper.selectById(username);
        if (account == null) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername(account.getUsername());
        accountVO.setEmail(account.getEmail());
        accountVO.setFirstName(account.getFirstName());
        accountVO.setLastName(account.getLastName());
        accountVO.setStatus(account.getStatus());
        accountVO.setAddress1(account.getAddress1());
        accountVO.setAddress2(account.getAddress2());
        accountVO.setCity(account.getCity());
        accountVO.setState(account.getState());
        accountVO.setZip(account.getZip());
        accountVO.setCountry(account.getCountry());
        accountVO.setPhone(account.getPhone());

        // 获取用户密码信息
        SignOn signOn = signOnMapper.selectById(username);
        if (signOn != null) {
            accountVO.setPassword(signOn.getPassword());
        }

        // 获取用户偏好设置信息
        Profile profile = profileMapper.selectById(username);
        if (profile != null) {
            accountVO.setLangPref(profile.getLangPref());
            accountVO.setFavoriteGory(profile.getFavCategory());
            accountVO.setMyListOpt(profile.getMyListOpt());
            accountVO.setBannerOpt(profile.getBannerOpt());
        }

        return accountVO;
    }

    @Override
    public void insertAccountInformation(AccountVO accountVO) {
        SignOn signOn = new SignOn();
        signOn.setUsername(accountVO.getUsername());
        signOn.setPassword(accountVO.getPassword());
        signOnMapper.insert(signOn);
    }

    @Override
    public void updateAccountInformation(AccountVO accountVO) {
        // 更新SignOn表
        SignOn signOn = new SignOn();
        signOn.setUsername(accountVO.getUsername());
        signOn.setPassword(accountVO.getPassword());
        signOnMapper.updateById(signOn);

        // 更新Account表
        Account account = setAccount(accountVO);
        accountMapper.updateById(account);

        // 更新Profile表
        Profile profile = setProfile(accountVO);
        profileMapper.updateById(profile);
    }

    @Override
    public Account setAccount(AccountVO accountVO) {
        Account account = new Account();
        account.setUsername(accountVO.getUsername());
        account.setEmail(accountVO.getEmail());
        account.setFirstName(accountVO.getFirstName());
        account.setLastName(accountVO.getLastName());
        account.setStatus(accountVO.getStatus());
        account.setAddress1(accountVO.getAddress1());
        account.setAddress2(accountVO.getAddress2());
        account.setCity(accountVO.getCity());
        account.setState(accountVO.getState());
        account.setZip(accountVO.getZip());
        account.setCountry(accountVO.getCountry());
        account.setPhone(accountVO.getPhone());
        return account;
    }

    @Override
    public SignOn setSign(AccountVO accountVO) {
        SignOn signOn = new SignOn();
        signOn.setUsername(accountVO.getUsername());
        signOn.setPassword(accountVO.getPassword());
        return signOn;
    }

    @Override
    public Profile setProfile(AccountVO accountVO) {
        Profile profile = new Profile();
        profile.setUserId(accountVO.getUsername());
        profile.setLangPref(accountVO.getLangPref());
        profile.setFavCategory(accountVO.getFavoriteGory());
        profile.setMyListOpt(accountVO.getMyListOpt());
        profile.setBannerOpt(accountVO.getBannerOpt());
        return profile;
    }
}
