package com.example.bitcoininfoapp.data.remote

import com.example.bitcoininfoapp.data.remote.bitcoin_info.BitcoinService
import com.example.bitcoininfoapp.data.remote.bitcoin_info.BitcoinServiceImpl

class RemoteRepository : RemoteRepo
    , BitcoinService by BitcoinServiceImpl()