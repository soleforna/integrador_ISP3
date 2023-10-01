import datetime

class DateFormat():
    
    @staticmethod
    def convert_date(date):
        return datetime.datetime.strftime(date,'%d/%m/%Y %H:%M:%Shs.')
